package com.orion.friendsroom.service.impl;

import com.orion.friendsroom.entity.DebtEntity;
import com.orion.friendsroom.entity.RoomEntity;
import com.orion.friendsroom.entity.Status;
import com.orion.friendsroom.entity.UserEntity;
import com.orion.friendsroom.exceptions.NotFoundException;
import com.orion.friendsroom.repository.DebtRepository;
import com.orion.friendsroom.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class DebtServiceImpl implements DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Transactional
    @Override
    public DebtEntity createDept(UserEntity user, RoomEntity room) {
        DebtEntity debt = new DebtEntity();
        debt.setCreated(new Date());
        debt.setUpdated(debt.getCreated());
        debt.setStatus(Status.ACTIVE);
        debt.setUser(user);
        debt.setRoom(room);
        debt.setSum(debtCalculation(room));
        debtRepository.save(debt);
        return debt;
    }

    @Transactional
    @Override
    public DebtEntity deleteDebt(UserEntity guest, RoomEntity room) {
        DebtEntity debt = debtRepository.findByRoomAndUser(room, guest);

        if (debt == null) {
            throw new NotFoundException("Debt not found!");
        }

        debt.setStatus(Status.DELETED);
        debt.setUpdated(new Date());
        debtRepository.save(debt);

        return debt;
    }

    private Double debtCalculation(RoomEntity room) {
        return room.getTotalAmount()/(room.getUsers().size() - 1);
    }
}
