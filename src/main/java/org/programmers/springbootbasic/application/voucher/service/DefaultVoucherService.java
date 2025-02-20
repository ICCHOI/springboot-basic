package org.programmers.springbootbasic.application.voucher.service;

import org.programmers.springbootbasic.application.voucher.controller.api.CreateVoucherRequest;
import org.programmers.springbootbasic.application.voucher.controller.api.UpdateVoucherRequest;
import org.programmers.springbootbasic.application.voucher.model.Voucher;
import org.programmers.springbootbasic.application.voucher.repository.VoucherRepository;
import org.programmers.springbootbasic.core.exception.DuplicateObjectKeyException;
import org.programmers.springbootbasic.core.exception.NotFoundIdException;
import org.programmers.springbootbasic.core.exception.NotUpdateException;
import org.programmers.springbootbasic.application.voucher.controller.VoucherConverter;
import org.programmers.springbootbasic.application.voucher.model.VoucherType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DefaultVoucherService implements VoucherService {
    private final VoucherRepository voucherRepository;

    private final VoucherConverter voucherConverter;

    public DefaultVoucherService(VoucherRepository voucherRepository, VoucherConverter voucherConverter) {
        this.voucherRepository = voucherRepository;
        this.voucherConverter = voucherConverter;
    }

    public Voucher getVoucher(UUID voucherId) {
        return voucherRepository
                .findById(voucherId)
                .orElseThrow(() -> new NotFoundIdException("주문을 찾을 수 없습니다."));
    }

    public List<Voucher> getVoucherList() {
        return voucherRepository.findAll();
    }

    public List<Voucher> getVoucherListByVoucherType(VoucherType voucherType) {
        return voucherRepository.findByVoucherType(voucherType);
    }

    public List<Voucher> getVoucherListOrderByCreatedAt() {
        return voucherRepository.findByCreatedAt();
    }

    public Voucher createVoucher(CreateVoucherRequest voucherRequest) {
        UUID voucherId = UUID.randomUUID();
        if (checkVoucherExist(voucherId)) {
            throw new DuplicateObjectKeyException("이미 존재하는 바우처 입니다.");
        }
        var voucher = voucherConverter.convertVoucher(voucherRequest);

        return voucherRepository.insert(voucher);
    }

    public Voucher updateVoucher(UpdateVoucherRequest updateVoucherRequest)  {
        if (!checkVoucherExist(updateVoucherRequest.voucherId())) {
            throw new NotUpdateException("업데이트 할 바우처가 없습니다.");
        }
        Voucher updateVoucher = getVoucher(updateVoucherRequest.voucherId())
                .changeValue(updateVoucherRequest.value());
        return voucherRepository.update(updateVoucher);
    }

    public void deleteVoucher(UUID voucherId) {
        voucherRepository.deleteById(voucherId);
    }

    private boolean checkVoucherExist(UUID voucherId) {
        return voucherRepository.getCountByVoucherId(voucherId) > 0;
    }
}
