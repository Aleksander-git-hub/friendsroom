package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.email.MailSender;
import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.enums.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.repository.DebtRepository;
import com.orion.friendsroom.repository.RoomRepository;
import com.orion.friendsroom.repository.UserRepository;
import com.orion.friendsroom.service.DebtService;
import com.orion.friendsroom.service.MessageGenerate;
import org.apache.commons.math3.util.Precision;
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

    @Autowired
    private MailSender mailSender;

    private Boolean isFromRepayDebt = false;

    @Transactional
    @Override
    public DebtEntity createDept(UserEntity guest, RoomEntity room,
                                 Double amount, UserEntity ownerOfMoney) {
        DebtEntity debt = debtRepository
                .findByRoomAndUserAndWhoOwesMoneyAndStatus(room, guest, ownerOfMoney, Status.ACTIVE);

        if (debt == null || debt.getStatus() == Status.DELETED) {
            debt = new DebtEntity();
            debt.setCreated(new Date());
            debt.setUpdated(debt.getCreated());
            debt.setStatus(Status.ACTIVE);
            debt.setUser(guest);
            debt.setRoom(room);
            debt.setWhoOwesMoney(ownerOfMoney);
            debt.setSum(Precision.round((debtCalculation(room, amount, isFromRepayDebt)), 2));
            debtRepository.save(debt);
            return debt;
        }

        debt.setSum(Precision.round(
                (debt.getSum() + (debtCalculation(room, amount, isFromRepayDebt))), 2)
        );
        debt.setUpdated(new Date());
        debtRepository.save(debt);
        guest.setTotalAmount(Precision.round(
                (guest.getTotalAmount() + debtCalculation(room, amount, isFromRepayDebt)), 2)
        );
        guest.setUpdated(new Date());
        userRepository.save(guest);
        room.setUpdated(new Date());
        roomRepository.save(room);

        return null;
    }

    @Transactional
    @Override
    public DebtEntity repayDebt(UserEntity guest, RoomEntity room, Double amount, UserEntity ownerOfMoney) {
        DebtEntity debt = debtRepository
                .findByRoomAndUserAndWhoOwesMoneyAndStatus(room, guest, ownerOfMoney, Status.ACTIVE);

        if (debt == null) {
            throw new NotFoundException("Debt not found!");
        }

        if (debt.getStatus() == Status.DELETED) {
            throw new NotFoundException("The Debt closed!");
        }

        Integer result = deductionOfDebt(debt, amount);

        if (result == -1) {
            Double reverseAmount = Precision.round(
                    (debt.getSum() - amount) * (-1), 2);
            isFromRepayDebt = true;
            DebtEntity reverseDebt = createDept(ownerOfMoney, room, reverseAmount, guest);
            roomService.checkingDebt(ownerOfMoney, room, reverseDebt);
            String message = MessageGenerate.getMessageAboutOverpayment(ownerOfMoney, reverseDebt, room);
            mailSender.send(ownerOfMoney.getEmail(), "Overpayment", message);
        }

        debt.setUpdated(new Date());
        debtRepository.save(debt);
        return debt;
    }

    private Integer deductionOfDebt(DebtEntity debt, Double amount) {
        Double totalDebt = Precision.round(
                (debt.getSum()), 2
        );

        if ((totalDebt - amount) == Precision.round(0D, 2)) {
            debt.setStatus(Status.DELETED);
            return 0;
        }

        if ((totalDebt - amount) > Precision.round(0D, 2)) {
            debt.setSum(Precision.round((totalDebt - amount), 2));
            return 1;
        }

        debt.setStatus(Status.DELETED);
        return -1;
    }

    private Double debtCalculation(RoomEntity room, Double amount, Boolean isFromRepayDebt) {
        if (isFromRepayDebt) {
            return amount;
        }
        return Precision.round((amount/room.getUsers().size()), 2);
    }
}
