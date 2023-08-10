package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.modelmbean.ModelMBeanOperationInfo;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;
    @Autowired
    private ITransferService transferService;
    @Autowired
    private IWithdrawService withdrawService;

    @Autowired
    private IDepositService depositService;


    @GetMapping
    public String showListCustomer(Model model) {
        List<Customer> customers = customerService.findAllByDeletedIsFalse();
        model.addAttribute("customers", customers);

        return "/customer/list";
    }

    @GetMapping("/create")
    public String showCreate(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "/customer/create";
    }

    @PostMapping("/create")
    public String doCreate(@ModelAttribute Customer customer, BindingResult bindingResult, Model model) {

        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "/customer/create";
        }


        String email = customer.getEmail();
        Boolean existsEmail = customerService.existsByEmail(email);

        if (existsEmail) {
            model.addAttribute("notValid", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "/customer/create";
        }

        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);

        model.addAttribute("customer", new Customer());
        model.addAttribute("success", true);
        model.addAttribute("messages", "thêm thành công");

        return "/customer/create";
    }

    @GetMapping("/edit/{id}")
    public String showUpdate(@PathVariable String id, Model model) {
        try {
            Long customerId = Long.parseLong(id);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (!customerOptional.isPresent()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "ID khách hàng không tồn tại");
                model.addAttribute("customer", new Customer());
                return "/customer/edit";
            }

            Customer customer = customerOptional.get();

            model.addAttribute("customer", customer);

            return "/customer/edit";
        } catch (Exception e) {
            return "/customer/edit";
        }
    }

    @PostMapping("/edit/{id}")
    public String toUpdate(@PathVariable Long id, Model model, @ModelAttribute Customer customer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        Optional<Customer> customerOptional = customerService.findById(id);
        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "/customer/edit";
        }


        String email = customer.getEmail();
        List<Customer> customers = customerService.findAllByIdNotAndDeletedIsFalse(id);
        for (Customer c : customers) {
            String emailC = c.getEmail();
            if (email.equals(emailC)) {
                model.addAttribute("notValid", true);
                model.addAttribute("message", "Email đã tồn tại");
                return "/customer/edit";
            }
        }


        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id không tồn tại");
            return "/customer/edit";
        } else {
            customer.setId(id);
            customer.setBalance(customerOptional.get().getBalance());
            customerService.save(customer);
            model.addAttribute("customer", customer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "sửa thành công");

            return "redirect:/customers";
        }


    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {

        try {
            Optional<Customer> customerOptional = customerService.findById(id);
            Customer customer = customerOptional.get();
            customer.setDeleted(true);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công");

            return "redirect:/customers";
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id không tồn tại");
            return "/errors/error";
        }
    }

    @GetMapping("/deposit/{id}")
    public String showDeposit(@PathVariable String id, Model model) {

        try {
            Long customerId = Long.parseLong(id);
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (!customerOptional.isPresent()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "ID khách hàng không tồn tại");
                model.addAttribute("deposit", new Deposit());
                return "/customer/deposit";
            }

            Customer customer = customerOptional.get();

            Deposit deposit = new Deposit();
            deposit.setCustomer(customer);

            model.addAttribute("deposit", deposit);

            return "/customer/deposit";
        } catch (Exception e) {
            return "/errors/error";
        }
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@PathVariable Long customerId, Model model, @ModelAttribute Deposit deposit, BindingResult bindingResult) {

        new Deposit().validate(deposit, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "/customer/deposit";
        }

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID khách hàng không tồn tại");
            return "/customer/deposit";
        } else {
            try {
                Customer customer = customerService.deposit(deposit);

                deposit.setCustomer(customer);

                model.addAttribute("deposit", deposit);

                model.addAttribute("success", true);
                model.addAttribute("message", "Nạp tiền thành công");
            } catch (Exception ex) {
                return "/errors/error";
            }
        }

        return "/customer/deposit";
    }

    @GetMapping("/withdraw/{id}")
    public String showWithdraw(@PathVariable Long id, Model model) {


        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id khách hàng không tồn tại");
            model.addAttribute("withdraw", new Withdraw());

            return "/customer/withdraw";
        } else {
            Customer customer = customerOptional.get();
            Withdraw withdraw = new Withdraw();
            withdraw.setCustomer(customer);

            model.addAttribute("withdraw", withdraw);
        }
        return "/customer/withdraw";


    }

    @PostMapping("/withdraw/{customerId}")
    public String toWithdraw(@PathVariable Long customerId, Model model, @ModelAttribute Withdraw withdraw, BindingResult bindingResult) {
        new Withdraw().validate(withdraw, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return "/customer/withdraw";
        }
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Id không tồn tại");
            return "/customer/withdraw";
        } else {
            try {
                Customer customer = customerService.withdraw(withdraw);
                withdraw.setCustomer(customer);

                model.addAttribute("withdraw", withdraw);

                model.addAttribute("success", true);
                model.addAttribute("message", "Rút tiền thành công");
            } catch (Exception e) {
                return "/errors/error";
            }
        }
        return "/customer/withdraw";

    }

    @GetMapping("/transfer/{customerId}")
    public String showTransfer(@PathVariable Long customerId, Model model) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            model.addAttribute("error", true);
            model.addAttribute("messages", "ID khách hàng không tồn tại");
            Transfer transfer = new Transfer();
            model.addAttribute("transfer", transfer);
            return "/customer/transfer";
        } else {
            Customer customer = customerOptional.get();
            Transfer transfer = new Transfer();
            transfer.setSender(customer);

            model.addAttribute("transfer", transfer);
            List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(customerId);
            model.addAttribute("recipients", recipients);
        }

        return "/customer/transfer";
    }


    @PostMapping("/transfer/{customerId}")
    public String toTransfer(@PathVariable Long customerId, Model model, @ModelAttribute Transfer transfer, BindingResult bindingResult) {
            new Transfer().validate(transfer, bindingResult);

            if (bindingResult.hasFieldErrors()) {
                List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(customerId);
                model.addAttribute("recipients", recipients);
                model.addAttribute("transfer", transfer);

                return "/customer/transfer";
            }

            Optional<Customer> customerOptional = customerService.findById(customerId);
            List<Customer> recipients = customerService.findAllByIdNotAndDeletedIsFalse(customerId);

            model.addAttribute("recipients", recipients);
            model.addAttribute("transfer", transfer);

            if (!customerOptional.isPresent()) {
                model.addAttribute("error", true);
                model.addAttribute("messages", "ID người chuyển không tồn tại");

                return "/customer/transfer";
            }

            Long idRecipient = transfer.getRecipient().getId();
            Optional<Customer> recipientOptional = customerService.findById(idRecipient);

            if (!recipientOptional.isPresent()) {
                model.addAttribute("error", true);
                model.addAttribute("messages", "ID người nhận không tồn tại");

                return "/customer/transfer";
            }

            if (idRecipient.equals(customerId)) {
                model.addAttribute("error", true);
                model.addAttribute("messages", "ID người nhận trùng người chuyển");

                return "/customer/transfer";
            }

            BigDecimal senderCurrentBalance = customerOptional.get().getBalance();


            String transferAmountStr = String.valueOf(transfer.getTransferAmount());
            BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferAmountStr));


            long fees = 10L;
            BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
            BigDecimal transactionAmount = transferAmount.add(feesAmount);

            if (transferAmount.compareTo(senderCurrentBalance) > 0) {
                model.addAttribute("error", true);
                model.addAttribute("messages", "Tài khoản không đủ tiền để chuyển");

                return "/customer/transfer";
            }

            transfer.setSender(customerOptional.get());
            transfer.setFees(fees);
            transfer.setTransferAmount(transferAmount);
            transfer.setFeesAmount(feesAmount);
            transfer.setTransactionAmount(transactionAmount);

            customerService.transfer(transfer);

            transfer.setTransferAmount(BigDecimal.ZERO);
            transfer.setTransactionAmount(BigDecimal.ZERO);

            model.addAttribute("transfer", transfer);
            model.addAttribute("success", true);
            model.addAttribute("messages", "Chuyển tiền thành công");

            return "/customer/transfer";
    }



}
