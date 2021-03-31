package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.repository.DebtRepository;
import com.orion.friendsroom.repository.RoomRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DebtServiceImpl implements DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomServiceImpl roomService;

    @Transactional
    @Override
    public DebtEntity createDept(UserEntity guest, RoomEntity room,
                                 Double amount, UserEntity ownerOfMoney) {
        DebtEntity debt = debtRepository
                .findByRoomAndUserAndWhoOwesMoney(room, guest, ownerOfMoney);

        if (debt == null) {
            debt = new DebtEntity();
            debt.setCreated(new Date());
            debt.setUpdated(debt.getCreated());
            debt.setStatus(Status.ACTIVE);
            debt.setUser(guest);
            debt.setRoom(room);
            debt.setWhoOwesMoney(ownerOfMoney);
            debt.setSum(debtCalculation(room, amount));
            debtRepository.save(debt);
            return debt;
        }

        debt.setSum(debt.getSum() + debtCalculation(room, amount));
        debt.setUpdated(new Date());
        debtRepository.save(debt);
        guest.setTotalAmount(guest.getTotalAmount() +
                debtCalculation(room, amount));
        guest.setUpdated(new Date());
        userRepository.save(guest);
        room.setUpdated(new Date());
        roomRepository.save(room);

        return null;
    }

    @Transactional
    @Override
    public DebtEntity deleteDebt(UserEntity guest, RoomEntity room, Double amount, UserEntity ownerOfMoney) {
        DebtEntity debt = debtRepository
                .findByRoomAndUserAndWhoOwesMoney(room, guest, ownerOfMoney);

        if (debt == null) {
            throw new NotFoundException("Debt not found!");
        }

        if (debt.getStatus().equals(Status.DELETED)) {
            throw new NotFoundException("The Debt closed!");
        }

        int result = deductionOfDebt(debt, amount);

        if (result == -1) {
            Double reverseAmount = (debt.getSum() - amount) * (-1);
            debtCalculation(room, reverseAmount);
            DebtEntity reverseDebt = createDept(ownerOfMoney, room, reverseAmount, guest);

            roomService.checkingDebt(ownerOfMoney, room, guest, reverseAmount, reverseDebt);
        }

        debt.setUpdated(new Date());
        debtRepository.save(debt);
        return debt;
    }

    private int deductionOfDebt(DebtEntity debt, Double amount) {
        Double totalDebt = debt.getSum();

        if ((totalDebt - amount) == 0) {
            debt.setStatus(Status.DELETED);
            return 0;
        }

        if ((totalDebt - amount) > 0) {
            debt.setSum(totalDebt - amount);
            return 1;
        }

        debt.setStatus(Status.DELETED);
        return -1;
    }

    private Double debtCalculation(RoomEntity room, Double amount) {
        return amount/room.getUsers().size();
    }
}
