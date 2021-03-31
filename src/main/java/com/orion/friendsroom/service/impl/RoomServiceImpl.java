package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.dto.room.AmountDto;
import com.orion.friendsroom.dto.room.RoomCreationDto;
import com.orion.friendsroom.dto.room.RoomNameDto;
import com.orion.friendsroom.dto.user.EmailUserDto;
import com.orion.friendsroom.dto.user.RepayDebtDto;
import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.ForbiddenError;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.mapper.RoomMapper;
import com.orion.friendsroom.repository.RoomRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.service.CurrentUserService;
import com.orion.friendsroom.service.DebtService;
import com.orion.friendsroom.service.MessageGenerate;
import com.orion.friendsroom.service.RoomService;
import com.orion.friendsroom.service.validation.EntityValidator;
import com.orion.friendsroom.service.validation.RoomValidator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DebtService debtService;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private CurrentUserService currentUserService;

    @Transactional
    @Override
    public RoomEntity createRoom(RoomCreationDto roomCreationDto) {
        RoomValidator.validateRoom(roomCreationDto);

        UserEntity currentUser = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(currentUser);

        if (roomRepository.findByName(roomCreationDto.getName()) != null) {
            throw new NotFoundException("The Room with name already exist!");
        }

        RoomEntity creationRoom = roomMapper.toEntity(roomCreationDto);
        creationRoom.setTotalAmount(0D);
        creationRoom.setCreated(new Date());
        creationRoom.setUpdated(creationRoom.getCreated());
        creationRoom.setStatus(Status.NOT_CONFIRMED);
        creationRoom.setOwner(currentUser);
        creationRoom.setActivationCode(UUID.randomUUID().toString());
        creationRoom.setUsers(new ArrayList<>());
        creationRoom.getUsers().add(currentUser);
        creationRoom.setDebts(new ArrayList<>());
        roomRepository.save(creationRoom);

        currentUser.getUserRooms().add(creationRoom);
        currentUser.getRooms().add(creationRoom);
        currentUser.setUpdated(new Date());
        userRepository.save(currentUser);

        String message = MessageGenerate.getMessageForRoom(creationRoom);
        mailSender.send(currentUser.getEmail(), "Creating Room", message);

        return creationRoom;
    }

    @Transactional
    @Override
    public void activateRoom(String code) {
        RoomEntity roomEntity = roomRepository.findByActivationCode(code);
        roomEntity.setActivationCode(null);
        roomEntity.setStatus(Status.ACTIVE);
        roomRepository.save(roomEntity);
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll().stream()
                .filter(roomEntity -> roomEntity.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());
    }

    @Override
    public RoomEntity getRoomById(Long roomId) {
        RoomEntity existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found with id: " + roomId));

        RoomValidator.validateStatus(existingRoom);

        return existingRoom;
    }

    @Override
    public RoomEntity getRoomByName(RoomNameDto roomName) {
        RoomEntity existingRoom = roomRepository.findByName(roomName.getName());

        if (existingRoom == null) {
            throw new NotFoundException("Room not found by name: " + roomName.getName());
        }

        RoomValidator.validateStatus(existingRoom);

        return existingRoom;
    }

    @Transactional
    @Override
    public RoomEntity addGuestToRoom(EmailUserDto emailUserDto, Long roomId) {
        if (StringUtils.isEmpty(emailUserDto.getEmail())) {
            throw new NotFoundException("Enter user's email!");
        }

        UserEntity owner = currentUserService.getCurrentUser();
        RoomEntity existingRoom = getRoomById(roomId);

        RoomValidator.validateStatus(existingRoom);
        RoomValidator.validateOwner(owner, existingRoom);

        UserEntity addedUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (addedUser == null) {
            throw new NotFoundException("Added user not found: " + emailUserDto.getEmail());
        }

        EntityValidator.validateStatus(addedUser);

        if (existingRoom.getUsers().contains(addedUser)) {
            throw new NotFoundException("This guest is already here!");
        }

        addedUser.getRooms().add(existingRoom);
        addedUser.setUpdated(new Date());
        userRepository.save(addedUser);

        existingRoom.getUsers().add(addedUser);
        existingRoom.setUpdated(new Date());
        roomRepository.save(existingRoom);

        String message = MessageGenerate.getMessageAddGuest(addedUser, existingRoom);
        mailSender.send(addedUser.getEmail(), "Adding to the Room", message);

        return existingRoom;
    }

    @Override
    public List<UserEntity> getGuestsOfRoom(Long roomId) {
        RoomEntity existingRoom = getRoomById(roomId);

        return existingRoom.getUsers();
    }

    @Transactional
    @Override
    public RoomEntity deleteGuestFromRoom(EmailUserDto emailUserDto, Long roomId) {
        UserEntity owner = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(owner);

        RoomEntity existingRoom = getRoomById(roomId);
        RoomValidator.validateOwner(owner, existingRoom);

        UserEntity existingUser = userRepository.findByEmail(emailUserDto.getEmail());

        if (existingUser == null) {
            throw new NotFoundException("Deleted user not found: " + emailUserDto.getEmail());
        }

        if (!existingRoom.getUsers().contains(existingUser)) {
            throw new NotFoundException("This guest not found in room");
        }

        existingUser.getRooms().remove(existingRoom);
        existingUser.setUpdated(new Date());
        userRepository.save(existingUser);

        existingRoom.getUsers().remove(existingUser);
        existingRoom.setUpdated(new Date());
        roomRepository.save(existingRoom);

        String message = MessageGenerate.getMessageDeleteGuest(existingUser, existingRoom);
        mailSender.send(existingUser.getEmail(), "Exclusion", message);

        return existingRoom;
    }

    @Override
    public RoomEntity addAmountToRoom(AmountDto amountDto, Long roomId) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(currentUser);
        RoomEntity room = getRoomById(roomId);

        RoomValidator.validateTotalAmount(amountDto.getTotalAmount());
        RoomValidator.validateStatus(room);
        RoomValidator.validateListOfGuests(room);
        RoomValidator.validateGuest(currentUser, room);

        room.setTotalAmount(room.getTotalAmount() + amountDto.getTotalAmount());

        List<UserEntity> usersInRoom = room.getUsers();

        for (int i = 0; i < usersInRoom.size(); i++) {
            UserEntity guest = usersInRoom.get(i);

            if (guest.equals(currentUser)) {
                continue;
            }

            DebtEntity debt = debtService.createDept(guest, room,
                    amountDto.getTotalAmount(), currentUser);

            checkingDebt(guest, room, currentUser, amountDto.getTotalAmount(), debt);
        }

        return room;
    }

    @Transactional
    public void checkingDebt(UserEntity guest, RoomEntity room, UserEntity currentUser,
             Double totalAmount, DebtEntity debt) {
        if (debt != null) {
            guest.getDebts().add(debt);
            guest.setTotalAmount((guest.getTotalAmount() + debt.getSum()));
            guest.setUpdated(new Date());
            userRepository.save(guest);
            room.getDebts().add(debt);
            room.setUpdated(new Date());
            roomRepository.save(room);
        }

        String message = MessageGenerate.getMessageDebtForGuest(
                guest,
                room,
                currentUser,
                totalAmount
        );
        mailSender.send(guest.getEmail(), "New Debt", message);
    }

    @Transactional
    @Override
    public RoomEntity repayDebtToUser(RepayDebtDto repayDebtDto, Long roomId) {
        UserEntity currentUser = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(currentUser);

        RoomEntity room = getRoomById(roomId);
        RoomValidator.validateGuest(currentUser, room);
        if (StringUtils.isEmpty(repayDebtDto.getToWhom())) {
            throw new NotFoundException("Enter who to repay the Debt.");
        }
        RoomValidator.validateTotalAmount(repayDebtDto.getAmount());

        UserEntity ownerOfMoney = userRepository.findByEmail(repayDebtDto.getToWhom());

        if (ownerOfMoney == null ||
            !room.getUsers().contains(ownerOfMoney)) {
            throw new NotFoundException("User not found with email: " +
                    repayDebtDto.getToWhom());
        }

        DebtEntity debt = debtService.repayDebt(currentUser, room, repayDebtDto.getAmount(), ownerOfMoney);

        room.setUpdated(new Date());

        currentUser.setTotalAmount(currentUser.getTotalAmount() - debt.getSum()); // check this
        currentUser.setUpdated(new Date());

        userRepository.save(currentUser);
        roomRepository.save(room);

        sendMessageRepayDebt(currentUser, debt, room);

        return room;
    }

    private void sendMessageRepayDebt(UserEntity currentUser, DebtEntity debt, RoomEntity room) {
        String messageForDebtors = null;

        if (debt.getStatus().equals(Status.DELETED)) {
            messageForDebtors = MessageGenerate.getMessageDropDebtFromGuest(currentUser, debt, room);
        }

        if (debt.getSum() >= 0) {
            messageForDebtors = MessageGenerate.getMessagePartialDebtClosure(currentUser, debt, room);
            String messageForOwner = MessageGenerate
                    .getMessageWhomOwnsMoney(currentUser, debt, room);
            mailSender.send(debt.getWhoOwesMoney().getEmail(), "Partially closure debt", messageForOwner);
        }

        mailSender.send(currentUser.getEmail(), "Repay Debt", messageForDebtors);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        UserEntity owner = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(owner);

        RoomEntity existingRoom = getRoomById(roomId);
        RoomValidator.validateRoomBeforeDeleting(existingRoom);

        if (!existingRoom.getOwner().equals(owner)) {
            throw new ForbiddenError("Access denied!");
        }

        if (existingRoom.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("This room is already deleted.");
        }

        owner.getUserRooms().remove(existingRoom);
        owner.getRooms().remove(existingRoom);
        owner.setUpdated(new Date());

        existingRoom.setStatus(Status.DELETED);
        existingRoom.setUpdated(new Date());

        existingRoom.getUsers().forEach(user -> {
            String message = MessageGenerate.getMessageForGuests(user, existingRoom);
            mailSender.send(user.getEmail(), "Deleting a Room", message);
        });

        userRepository.save(owner);
        roomRepository.save(existingRoom);
    }

    @Transactional
    @Override
    public RoomEntity updateRoomById(RoomCreationDto roomCreationDto, Long roomId) {
        UserEntity owner = currentUserService.getCurrentUser();
        EntityValidator.validateCurrentUser(owner);

        RoomValidator.validateRoom(roomCreationDto);
        RoomEntity updatingRoom = getRoomById(roomId);

        if (!updatingRoom.getOwner().equals(owner)) {
            throw new ForbiddenError("Access denied!");
        }

        roomMapper.updateRoomEntityFromRoomDto(roomCreationDto, updatingRoom);
        updatingRoom.setUpdated(new Date());
        roomRepository.save(updatingRoom);

        String message = MessageGenerate.getMessageForUpdateRoom(updatingRoom);
        mailSender.send(owner.getEmail(), "Updating Room", message);

        return updatingRoom;
    }
}
