import me.yves.Customer;

class CustomersTesting {


    void execute() {
        System.out.println("Testing Customer Class\n");
        Customer c1 = new Customer(20, 1, 100, 1000, true);
        System.out.println("Customer with specified attributes:\n" + c1);
        Customer c2 = Customer.randomCustomer();
        System.out.println("Customer with random attributes:\n" + c2);
        System.out.println("Testing 'equals' : " + !c1.equals(c2));
        Customer c3 = Customer.randomCustomer(999);
        System.out.println("Customer with random attributes but fixed seed:\n" + c3);
        System.out.println("Testing 'equals' : " + !c2.equals(c3));
        System.out.println("Testing 'equals' : " + c3.equals(Customer.randomCustomer(999)));
        Customer c4 = Customer.copyFromCustomer(c2);
        System.out.println("Testing 'clone' and 'equals' : " + c2.equals(c4));
//        c4.reset(); // commenting the reset customer because the original test was false
//        //(resetting set all to 0 but creating a customer without passing args create a random one)
//        System.out.println("Testing 'clearAllCustomer' : " + c4.equals(Customer.randomCustomer()));
        System.out.println();
    }
}
