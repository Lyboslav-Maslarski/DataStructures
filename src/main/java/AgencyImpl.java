import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class AgencyImpl implements Agency {

    Map<String, Invoice> invoiceBySerialNumber;
    Map<LocalDate, Set<Invoice>> invoiceByDueDate;
    Set<Invoice> payed;

    public AgencyImpl() {
        invoiceBySerialNumber = new HashMap<>();
        invoiceByDueDate = new HashMap<>();
        payed = new HashSet<>();
    }

    @Override
    public void create(Invoice invoice) {
        if (contains(invoice.getNumber())) {
            throw new IllegalArgumentException();
        }
        invoiceBySerialNumber.put(invoice.getNumber(), invoice);
        LocalDate dueDate = invoice.getDueDate();
        invoiceByDueDate.putIfAbsent(dueDate, new HashSet<>());
        invoiceByDueDate.get(dueDate).add(invoice);
    }

    @Override
    public boolean contains(String number) {
        return invoiceBySerialNumber.containsKey(number);
    }

    @Override
    public int count() {
        return invoiceBySerialNumber.size();
    }

    @Override
    public void payInvoice(LocalDate dueDate) {
        if (!invoiceByDueDate.containsKey(dueDate)) {
            throw new IllegalArgumentException();
        }
        invoiceByDueDate.get(dueDate).forEach(invoice -> {
            invoice.setSubtotal(0);
            payed.add(invoice);
        });
    }

    @Override
    public void throwInvoice(String number) {
        Invoice invoice = invoiceBySerialNumber.get(number);
        if (invoice == null) {
            throw new IllegalArgumentException();
        }
        invoiceByDueDate.get(invoice.getDueDate()).remove(invoice);
        invoiceBySerialNumber.remove(number);
    }

    @Override
    public void throwPayed() {
        payed.forEach(invoice -> {
            invoiceBySerialNumber.remove(invoice.getNumber());
            LocalDate dueDate = invoice.getDueDate();
            if (invoiceByDueDate.containsKey(dueDate)) {
                invoiceByDueDate.get(dueDate).remove(invoice);
            }
        });
    }

    @Override
    public Iterable<Invoice> getAllInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        return invoiceBySerialNumber.values().stream()
                .filter(inv -> (inv.getIssueDate().isAfter(startDate) || inv.getIssueDate().equals(startDate)) &&
                               inv.getIssueDate().isBefore(endDate) || inv.getIssueDate().equals(endDate))
                .sorted((l, r) -> {
                    if (l.getIssueDate().isEqual(r.getIssueDate())) {
                        return l.getDueDate().compareTo(r.getDueDate());
                    }
                    return l.getIssueDate().compareTo(r.getIssueDate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> searchByNumber(String number) {
        List<Invoice> list = invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getNumber().contains(number)).collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return list;
    }

    @Override
    public Iterable<Invoice> throwInvoiceInPeriod(LocalDate startDate, LocalDate endDate) {
        List<Invoice> list = invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getDueDate().isAfter(startDate) && invoice.getDueDate().isBefore(endDate))
                .collect(Collectors.toList());

        if (list.isEmpty()) {
            throw new IllegalArgumentException();
        }

        list.forEach(invoice -> {
            invoiceBySerialNumber.remove(invoice.getNumber());
            invoiceByDueDate.remove(invoice.getDueDate());
        });
        return list;
    }

    @Override
    public Iterable<Invoice> getAllFromDepartment(Department department) {
        return invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getDepartment().equals(department))
                .sorted((l, r) -> {
                    if (l.getSubtotal() != r.getSubtotal()) {
                        return Double.compare(r.getSubtotal(), l.getSubtotal());
                    }
                    return l.getIssueDate().compareTo(r.getIssueDate());
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Invoice> getAllByCompany(String companyName) {
        return invoiceBySerialNumber.values().stream()
                .filter(invoice -> invoice.getCompanyName().equals(companyName))
                .sorted((l, r) -> r.getNumber().compareTo(l.getNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public void extendDeadline(LocalDate endDate, int days) {
        if (!invoiceByDueDate.containsKey(endDate)) {
            throw new IllegalArgumentException();
        }
        Set<Invoice> invoices = invoiceByDueDate.get(endDate);
        LocalDate dueDate = endDate.plusDays(days);
        invoices.forEach(invoice -> invoice.setDueDate(dueDate));
        invoiceByDueDate.remove(endDate);
        invoiceByDueDate.putIfAbsent(dueDate, new HashSet<>());
        invoices.forEach(invoice -> invoiceByDueDate.get(dueDate).add(invoice));
    }
}
