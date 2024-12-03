import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.text.*;

class booking
{
    String name;
    String date;
    String time;

    booking (String name, String d, String t)
    {
        this.name = name;
        this.date = d;
        this.time = t;
    }

    String getname()
    {
        return name;
    }

    String getdate()
    {
        return date;
    }
    String gettime()
    {
        return time;
    }
}


class admin
{
    static String un, pw, name, pn, em, p, customerName = "", customerDate = "", customerTime = "";
    static String [] timeSlots = {"8:00am", "9:00am", "10:00am", "11:00am", "12:00pm", "1:00pm", "2:00pm", "3:00pm", "4:00pm", "5:00pm", "6:00pm"};

    static ArrayList<booking> bookings = new ArrayList<booking>();

    public int enter()
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Username: ");
        String un = scan.nextLine();
        int found = 0;
    
        try (BufferedReader br = new BufferedReader(new FileReader("admins.csv")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                if (values[0].equals(un))
                {
                    System.out.print("Password: ");
                    String pw = scan.nextLine();
                    if (values[1].equals(pw))
                    {
                        System.out.println("Login successful.");
                        found = 1;
                        data(values);
                        return 1;
                    }
                    else 
                    {
                        System.out.println("Incorrect password. Please try again.");
                        return 0;
                    }
                }  
            }
            if (found == 0)
            {
                System.out.println("Incorrect username. Please try again.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return 0;

    }

    public void viewcustomer()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv")))
        {
            String line;
            //System.out.println("\nCustomers are: ");
            int people = 0;
            while ((line = br.readLine()) != null)
            {
                if (people == 0)
                {
                    System.out.println("\nCustomers are: ");
                    people++;
                }
                String[] values = line.split(",");
                if (values[2].equals(" Name"))
                continue;
                System.out.println(people + " " + values[2]);
                people++;
            }
            if (people == 0)
            {
                System.out.println("No customers exist.");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void enabledisablecus(Scanner scan)
    {
        viewcustomer();
        System.out.print("Enter the sno of customer you want to enable/disable: ");
        int sno = scan.nextInt();
        ArrayList<String []> csvData = new ArrayList<String []>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                csvData.add(line.split(","));
            }
            
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        int people = 0;
        for (String [] row : csvData)
        {
            //System.out.println("row before" + row[24]);
            if (people == sno)
            {
               // System.out.println("Customer: " + row[2]);
                if (row[24].equals("true"))
                {
                    row[24] = "false";
                    System.out.println("Customer is now disabled and can not access their account.");
                   // System.out.println("row after: " + row[24]);
                    break;
                }
                else
                {
                    row[24] = "true";
                    System.out.println("Customer is now enabled and can access their account.");
                   // System.out.println("row after: " + row[24]);
                    break;
                }
            }
            people++;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv")))
        {
            for (String [] row : csvData)
            {
                bw.write(String.join(",", row) + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void data(String [] values)
    {
        un = values[0];
        pw = values[1];
        name = values[2];
        pn = values[3];
        em = values[4];
        p = values[5];
        terms.b = convertStringToArrayList(values[6]);
    }

    public void viewbookings()
    {
        if (terms.b.size() == 0) 
        {
            System.out.println("No bookings.");
            return;
        }
        System.out.println("\nBookings are: ");
        int sno = 1;
        for (booking b : terms.b)
        {
            System.out.println(sno + ". With " + b.getname() + " on " + b.getdate() + " at " + b.gettime());
        }
    }


    public ArrayList<booking> convertStringToArrayList(String s)
    {
        if (s.equals("No bookings")) return new ArrayList<booking>();
        ArrayList<booking> list = new ArrayList<booking>();
        String[] parts = s.split("\\\\");
        for (String part : parts)
        {
            String[] dateAndTime = part.split("-");
            list.add(new booking(dateAndTime[0], dateAndTime[1], dateAndTime[2]));
        }
        return list;
    }

    public void viewadmin()
    {
        try (BufferedReader br = new BufferedReader(new FileReader("admins.csv")))
        {
            String line;
            System.out.println("\nAdmins are: ");
            int people = 1;
            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                if (values[2].equals(" Name"))
                continue;
                System.out.println(people + " " + values[2]);
                people++;

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void createadmin(String un, String pw, String n, String pn, String em, String p)
    {
        try (FileWriter writer = new FileWriter("admins.csv", true))
        {
            //writer.append ("Username, Password, Name, Phone Number, Email ID, Position, Bookings\n");
            writer.append(un + "," + pw + "," + n + "," + pn + "," + em + "," + p + "," + convertArrayListToString(new ArrayList<booking>()) + "\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String adminbookingsupdate(String row)
    {
        ArrayList<booking> b1 = convertStringToArrayList(row);
        int found = 0;
        for (booking b2 : b1)
        {
            if (b2.getname().equals(customerName) && b2.getdate().equals(customerDate) && b2.gettime().equals(customerTime))
            {
                found = 1;
                b1.remove(b2);
                break;
            }
        }
        if (found == 0)
        {
            b1.add(new booking(customerName, customerDate, customerTime));
        }
        return convertArrayListToString(b1);
    }

    public void adminlogout()
    {
        try
        {
            List<String []> csvData = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("admins.csv")))
            {
                String line;
                while ((line = br.readLine()) != null)
                csvData.add(line.split(","));
            }

            boolean updated = false;
            for (String [] row : csvData)
            {
                if (row[0].equals(admin.un) | row[2].equals(admin.name))
                {
                    if (customerName != "")
                    row[6] = adminbookingsupdate(row[6]);
                    else
                    row[6] = convertArrayListToString(terms.b);
                    updated = true;
                    break;
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("admins.csv")))
            {
                for (String [] row : csvData)
                {
                    bw.write(String.join(",", row));
                    bw.newLine();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public static String convertArrayListToString(ArrayList<booking> list)
    {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty())
        return "No bookings";
        for (booking num : list)
        {
            String num1 =num.getname() + "-" + num.getdate() + "-" + num.gettime();
            sb.append(num1).append("\\");
        }
        return sb.toString();
    }

    public void customerBookingUpdate(booking b)
    {
        ArrayList<String []> csvData = new ArrayList<String []>();
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                csvData.add(line.split(","));    
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        ArrayList<booking> b1 = new ArrayList<>();
        String row1 = "";
        for (String [] row : csvData)
        {
            if (row[2].equals(b.getname()))
            {
                row1 = row[21];
                b1 = convertStringToArrayList(row1);
                for (int i = 0; i < b1.size(); i++)
                {
                    if (b1.get(i).getname().equals(name) && b1.get(i).getdate().equals(b.getdate()) && b1.get(i).gettime().equals(b.gettime()))
                    {
                    b1.remove(b1.get(i));
                    }
                }
                row[21] = convertArrayListToString(b1);
                break;
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv")))
        {
            for (String [] row : csvData)
            {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void removebooking(Scanner scan)
    {
        if (terms.b.isEmpty())
        {
            System.out.println("No bookings to remove.");
            return;
        }
        System.out.println("Your bookings are: ");
        for (int i = 0; i < terms.b.size(); i++)
        {
            System.out.println((i + 1) + ". " + terms.b.get(i).getdate() + " " + terms.b.get(i).gettime());
        }
        System.out.print("Enter option no.: ");
        int option = scan.nextInt();
        int removed = 0;
        for (int i = 0; i < terms.b.size(); i++)
        {
            if (i == option - 1)
            {
                removed = 1;
                customerBookingUpdate(terms.b.get(i));
                terms.b.remove(i);
                System.out.println("Booking removed");
                break;
            }
        }
        if (removed == 0)
        System.out.println("Booking not found");
    }

}


class LoanApplication
{
    String name;
    String address;
    double creditScore;
    double income;
    String loanType;
    double interestRate;
    double loanAmount;
    int loanTerm;
    String repaymentSchedule;
    static String [][] loans = {{"Personal Loan", "10.5"}, {"Home Loan", "7.0"}, {"Car Loan", "8.5"}, {"Education Loan", "6.5"}, {"Business Loan", "12.0"}}; //loantype interestrate

    LoanApplication(String loanType, double interestRate, double loanAmount, int loanTerm, String repaymentSchedule) 
    {
        this.loanType = loanType;
        this.interestRate = interestRate;
        this.loanAmount = loanAmount;
        this.loanTerm = loanTerm;
        this.repaymentSchedule = repaymentSchedule;
    }

    public static String convertArrayListToString(ArrayList<LoanApplication> list)
    {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty())
        return "No loan applications found.";
        for (LoanApplication loan : list)
        {
            String l1 = loan.loanType + ":" + loan.interestRate + ":" + loan.loanAmount + ":" + loan.loanTerm + ":" + loan.repaymentSchedule;
            sb.append(l1).append("\\");
        }
        return sb.toString();
    }

    public static ArrayList<LoanApplication> convertStringtoArrayList(String s)
    {
        ArrayList<LoanApplication> list = new ArrayList<>();
        if (s.equals("No loan applications found."))
        return new ArrayList<LoanApplication>();
        String[] parts = s.split("\\\\");
        for (String part : parts)
        {
            String[] loanDetails = part.split(":");
            String loanType = loanDetails[0];
            double interestRate = Double.parseDouble(loanDetails[1]);
            double loanAmount = Double.parseDouble(loanDetails[2]);
            int loanTerm = Integer.parseInt(loanDetails[3]);
            String repaymentSchedule = loanDetails[4];
            list.add(new LoanApplication(loanType, interestRate, loanAmount, loanTerm, repaymentSchedule));
        }
        return list;
    }
}


class LoanManagementSystem
{
    public void takeloan(Scanner scan)
    {
        double interestRate = 0.0;
        String loanType = "";
        boolean validLoanType = false;
        while (true)
        {
            System.out.println("\nAvailable loan types: ");
            int sno = 1;
            for (String[] lt : LoanApplication.loans)
            {
                System.out.println(sno + ". " + lt[0]);
                sno++;
            }
            System.out.print("Enter the loan type: ");
            int loanTypeoption = scan.nextInt(); 
            sno = 0;
            for (String[] lt : LoanApplication.loans)
            {
                if (sno == loanTypeoption - 1)
                {
                    loanType = lt[0];
                    interestRate = Double.parseDouble(lt[1]);
                    validLoanType = true;
                    break;
                }
                sno++;
            }
            if (!validLoanType)
            {
                System.out.println("Invalid loan type. Please select again");
            }
            else
            break;
        }
        if (validLoanType)
        {
            System.out.print("Enter the loan amount: ");
            double loanAmount = scan.nextDouble();
            System.out.print("Enter the loan term(in months): ");
            int loanTerm = scan.nextInt();
            System.out.print("Enter the repayment schedule(Monthly/Quarterly/Annually): ");
            String repaymentSchedule = scan.next();
            if (terms.balance >= 6500 && terms.aturnover >= 2000)
            {
                terms.l.add(new LoanApplication(loanType, interestRate, loanAmount, loanTerm, repaymentSchedule));
                System.out.println("Congratulations! Your loan has been Approved");
                viewloan();
                terms.balance += loanAmount;
                terms.t.add(new transactions("Loan", loanAmount, terms.balance));
            }
            else
            {
                System.out.println("Sorry! Your loan has been Declined");
            }
            //terms.l.add(new LoanApplication(loanType, interestRate, loanAmount, loanTerm, repaymentSchedule));
        }
    }

    public void viewloan()
    {
        if (terms.l.isEmpty())
        System.out.println("No loan applications found.");
        for (LoanApplication loan : terms.l)
        {
            System.out.println("\n--------------------------------------------");
            System.out.println("Loan Details: ");
            System.out.println("Loan Type: " + loan.loanType);
            System.out.println("Interest Rate: " + loan.interestRate);
            System.out.println("Loan Amount: " + loan.loanAmount);
            System.out.println("Loan Term: " + loan.loanTerm);
            System.out.println("Repayment Schedule: " + loan.repaymentSchedule);
            System.out.println("--------------------------------------------\n");
        }
    }

    public void LoanRepayment(Scanner scan)
    {
        if (terms.l.isEmpty())
        System.out.println("No loan applications found.");
        else
        {
            System.out.println("Available loan applications: ");
            int sno = 1;
            for (LoanApplication loan : terms.l)
            {
                System.out.println(sno + ". " + loan.loanType);
                sno++;
            }
            System.out.print("Enter the loan application number to repay: ");
            int loanAppNum = scan.nextInt();
            LoanApplication loan = terms.l.get(loanAppNum - 1);
            System.out.print("Enter the repayment amount: ");
            double repaymentAmount = scan.nextDouble();
            if (repaymentAmount < loan.loanAmount)
            {
                terms.balance -= repaymentAmount;
                terms.l.get(loanAppNum - 1).loanAmount -= repaymentAmount;
                terms.t.add(new transactions("Loan Repayment", repaymentAmount, terms.balance));
                System.out.println(repaymentAmount + "has been debited from your account");
                System.out.println("Remaining loan amount: " + terms.l.get(loanAppNum - 1).loanAmount);
            }
            else if (repaymentAmount == loan.loanAmount)
            {
                terms.balance -= repaymentAmount;
                terms.l.remove(loanAppNum - 1);
                terms.t.add(new transactions("Loan Repayment", repaymentAmount, terms.balance));
                System.out.println(repaymentAmount + "has been debited from your account");
                System.out.println("Loan has been fully repaid");
            }
            else if (repaymentAmount > loan.loanAmount)
            {
                terms.balance -= terms.l.get(loanAppNum - 1).loanAmount;
                terms.t.add(new transactions("Loan Repayment", terms.l.get(loanAppNum - 1).loanAmount, terms.balance));
                System.out.println(terms.l.get(loanAppNum - 1).loanAmount + "has been debited from your account");
                System.out.println("Loan has been fully repaid");
                terms.l.remove(loanAppNum - 1);
            }
        }
    }
}


class FixedDeposit
{
    String fdid;
    String type;
    double principalfdAmount;
    double fdinterestRate;
    int fdtenure;
    boolean isCumulative;
    Date startDate;
    Date maturityDate;

    FixedDeposit(String fdid, String type, double principalfdAmount, double fdinterestRate, int fdtenure, boolean isCumulative)
    {
        this.fdid = fdid;
        this.type = type;
        this.principalfdAmount = principalfdAmount;
        this.fdinterestRate = fdinterestRate;
        this.fdtenure = fdtenure;
        this.isCumulative = isCumulative;
        this.startDate = new Date();
        this.maturityDate = calculateMaturityDate();
    }

    public static String convertArrayListToString(ArrayList<FixedDeposit> list)
    {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty())
        return "No FDs";
        for (FixedDeposit num : list)
        {
            String num1 =num.fdid + ":" + num.type + ":" + num.principalfdAmount + ":" + num.fdinterestRate + ":" + num.fdtenure + ":" + num.isCumulative + ":" + num.startDate + ":" + num.maturityDate;
            sb.append(num1).append("\\");
        }
        return sb.toString();
    }

    public static ArrayList<FixedDeposit> convertStringToArrayList(String row)
    {
        if (row.equals("No FDs"))
        return new ArrayList<FixedDeposit>();
        ArrayList<FixedDeposit> fd1 = new ArrayList<FixedDeposit>();
        String[] fd2 = row.split("\\\\");
        for (String fd3 : fd2)
        {
            String[] fd4 = fd3.split(":");
            fd1.add(new FixedDeposit(fd4[0], fd4[1], Double.parseDouble(fd4[2]), Double.parseDouble(fd4[3]), Integer.parseInt(fd4[4]), Boolean.parseBoolean(fd4[5])));
        }
        return fd1;
    }

    public Date calculateMaturityDate()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, fdtenure);
        return calendar.getTime();
    }

    public double calculateInterest()
    {
        double rate = fdinterestRate / 100;
        if (isCumulative)
        {
            return (principalfdAmount * Math.pow(1 + rate / 12, fdtenure)) - principalfdAmount;
        }
        else
        return principalfdAmount * rate * fdtenure / 12;
    }
}

class FDManagementSystem
{
    static String fdtype;
    public void createFD()
    {
        Scanner scan = new Scanner(System.in);
        String fdid = UUID.randomUUID().toString().substring(0,8);
        int typeoption = 0;
        while (true)
        {
            System.out.println("\nFD Types are: \n1. Standard FD \n2. Tax Savings \n3. Cumulative \n4. Non-Cumulative");
            System.out.print("Enter FD Type you want to create (0: to not create any FD): ");
            typeoption = scan.nextInt();
            if (typeoption == 0)
            break;
            else if (typeoption == 1)
            fdtype = "Standard FD"; 
            else if (typeoption == 2)
            fdtype = "Tax Savings";
            else if (typeoption == 3)
            fdtype = "Cumulative";
            else if (typeoption == 4)
            fdtype = "Non-Cumulative";
            else
            System.out.println("Invalid Option. Select type again");
            if (typeoption >= 1 && typeoption <= 4)
            break;
        }
        if (typeoption == 0)
        return;
        else
        {
            double principalfdAmount = 0;
            while (true)
            {
                System.out.print("Enter the principal amount: ");
                principalfdAmount = scan.nextDouble();
                if (principalfdAmount > terms.balance)
                {
                    System.out.println("Insufficient balance.");
                }
                else
                {
                    terms.balance -= principalfdAmount;
                    break;
                }
            }
            System.out.print("Enter the interest rate: ");
            double fdinterestRate = scan.nextDouble();
            System.out.print("Enter the tenure (in months): ");
            int fdtenure = scan.nextInt();
            boolean isCumulative = (fdtype == "Cumulative");
            terms.fdRecords.add(new FixedDeposit(fdid, fdtype, principalfdAmount, fdinterestRate, fdtenure, isCumulative));
            terms.points += 20;
            System.out.println("FD created successfully.");
            terms.t.add(new transactions("FDcreated", principalfdAmount, terms.balance));
        }
    }

    public void viewFD()
    {
        if (terms.fdRecords.isEmpty())
        {
            System.out.println("\nNo Fixed Deposits found.\n");
            return;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        for (FixedDeposit fd : terms.fdRecords)
        {
            System.out.println("\n--------------------------------------------------");
            System.out.println("FD Details for FD ID: " + fd.fdid);
            System.out.println("FD Type: " + fd.type);
            System.out.println("Principal Amount: " + fd.principalfdAmount);
            System.out.println("Interest Rate: " + fd.fdinterestRate);
            System.out.println("Tenure: " + fd.fdtenure);
            System.out.println("Start Date: " + sdf.format(fd.startDate));
            System.out.println("Maturity Date: " + sdf.format(fd.maturityDate));
            System.out.println("Interest Earned: " + df.format(fd.calculateInterest()));
            System.out.println("Maturity Amount: " + df.format(fd.principalfdAmount + fd.calculateInterest()));
            System.out.println("--------------------------------------------------\n");
        }
    }

    public void Withdrawal(Scanner scan)
    {
        viewFD();
        System.out.print("Enter FD ID: ");
        String fdid = scan.next();
        for (FixedDeposit fd : terms.fdRecords)
        {
            if (fd.fdid.equals(fdid))
            {
                if (fd.maturityDate.after(new Date()))
                {
                    System.out.println("Do you want to: \n1. Continue with early Withdrawal \n2. Cancel Withdrawal");
                    int option = scan.nextInt();
                    if (option == 1)
                    {
                        double penaltyRate = 0.02;
                        double interestEarned = fd.calculateInterest();
                        double penaltyAmount = interestEarned * penaltyRate;
                        double payoutAmount = fd.principalfdAmount + interestEarned - penaltyAmount;
                        DecimalFormat df = new DecimalFormat("#.##");
                        System.out.println("Early Withdrawal Penalty: " + df.format(penaltyAmount));
                        System.out.println("Payout Amount after Penalty: " + df.format(payoutAmount));
                        terms.balance += payoutAmount;
                        terms.fdRecords.remove(fd);
                        terms.t.add(new transactions("FDwithdrawal", payoutAmount, terms.balance));
                        return;
                    }
                    else if (option == 2)
                    {
                        System.out.println("Withdrawal cancelled.");
                        return;
                    }
                    else
                    {
                        System.out.println("Invalid option. Try again.");
                    }
                }
                else
                {
                    double interestEarned = fd.calculateInterest();
                    double payoutAmount = fd.principalfdAmount + interestEarned;
                    DecimalFormat df = new DecimalFormat("#.##");
                    System.out.println("Payout Amount after Penalty: " + df.format(payoutAmount));
                    terms.balance += payoutAmount;
                    terms.fdRecords.remove(fd);
                    terms.t.add(new transactions("FDwithdrawal", payoutAmount, terms.balance));
                    terms.points += 100;
                    return;
                }
            }
        }
        System.out.println("FD not found");
    }
}


class AppointmentSystem
{
    public void makeappointment(Scanner scan)
    {
        admin a = new admin();

        a.viewadmin();
        System.out.print("\nWhom do you want to meet?(0: to not make any booking): ");
        int adminselected = scan.nextInt();
        if (adminselected == 0)
        return;
        int adminno = 0;
        String name = "";
        ArrayList<booking> bookings = new ArrayList<>(); //admins bookings
        try (BufferedReader br = new BufferedReader(new FileReader("admins.csv")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                if (adminno == adminselected)
                {
                    bookings = a.convertStringToArrayList(values[6]);
                    name = values[2];
                    break;
                }
                adminno++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        while (true)
        {
            System.out.print("Date you want to book an appointment for(dd/mm/yyyy): ");
            String date = scan.next();
            ArrayList<String> timings = new ArrayList<>();
            for (int i=0; i<bookings.size(); i++)
            {
                if (bookings.get(i).getdate().equals(date))
                timings.add(bookings.get(i).gettime());
            }
            if (timings.size() == admin.timeSlots.length)
            {
                System.out.println("No more slots available. Please select another date.");
            }
            else
            {
                System.out.println("\nAvailable timings: "); int sno = 1;
                for (int i=0; i<admin.timeSlots.length; i++)
                {
                    if (!timings.contains(admin.timeSlots[i]))
                    {
                        System.out.println(sno + ". " + admin.timeSlots[i]);
                        sno += 1;
                    }
                }
                System.out.print("Enter your choice: ");
                int choice = scan.nextInt();
                terms.b.add(new booking(name, date, admin.timeSlots[timings.size() + choice - 1]));
                admin.bookings = bookings; //terms.b.add(bookings);
                admin.name = name;
                admin.customerName = terms.accountHolder;
                admin.customerDate = date;
                admin.customerTime = admin.timeSlots[timings.size() + choice - 1];
                a.adminlogout();
                terms.points += 10;
                System.out.println("Booking confirmed with " + name + " on " + date + " at " + admin.timeSlots[timings.size() +choice - 1]);
                break;
            }

        }
    }

    public void cancelappointment(Scanner scan)
    {
        if (terms.b.isEmpty())
        {
            System.out.println("No appointments to cancel.");
            return;
        }
        viewappointments(scan);
        System.out.print("Which appointment do you want to cancel? (0: to not cancel any booking): ");
        int cancelchoice = scan.nextInt();
        if (cancelchoice == 0)
        {
            System.out.println("Appointment not cancelled.");
            return;
        }
        admin.name = terms.b.get(cancelchoice - 1).getname();
        admin.customerName = terms.accountHolder;
        admin.customerDate = terms.b.get(cancelchoice - 1).getdate();
        admin.customerTime = terms.b.get(cancelchoice - 1).gettime();
        admin a = new admin();
        a.adminlogout();
        terms.b.remove(cancelchoice - 1);
        System.out.println("Appointment cancelled");
    }

    public void viewappointments(Scanner scan)
    {
        if (terms.b.size() == 0)
        {
            System.out.println("\nNo appointments done yet");
            return;
        }
        System.out.println("\nYour appointments are: ");
        for (int i = 0; i < terms.b.size(); i++)
        {
            System.out.println((i + 1) + ". With " + terms.b.get(i).getname() + " on " + terms.b.get(i).getdate() + " at " + terms.b.get(i).gettime());
        }
    }
}


class login
{
    int enter()
    {
        Scanner scan  = new Scanner(System.in);
        System.out.print("Username: ");
        String username = scan.next();
        int found = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv")))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] values = line.split(",");
                if (values[0].equals(username))
                {
                    System.out.print("Password: ");
                    String password = scan.next();
                    if (values[1].equals(password))
                    {
                        data(values);
                        if (terms.enabled == false)
                        {
                            System.out.println("Your account is disabled. Please contact the admin.");
                            try (BufferedReader br1 = new BufferedReader(new FileReader("admins.csv")))
                            {
                                String line1;
                                System.out.println("Contact Details: ");
                                int lineno = 0;
                                while ((line1 = br1.readLine()) != null)
                                {
                                    if (lineno != 0)
                                    {
                                        String[] values1 = line1.split(",");
                                        System.out.println("Phone number: " + values1[3]);
                                        System.out.println("Email: " + values1[4]);
                                        return 0;
                                    }
                                    lineno++;
                                }
                            }
                        }
                        System.out.println("Login successful.");
                        //data(values);
                        found = 1;
                        return 1;
                    }
                    else
                    {
                        System.out.println("Incorrect password. Please try again.");
                        return 0;
                    }
                }
            }
            if (found == 0)
            {
                System.out.println("Username not found. Please try again.");
                return 0;
            }
        }
        catch (IOException e)
        {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return 0;
    }

    public void data(String [] values)
    {
        terms.un = values[0];
        terms.pw = values[1];
        terms.accountHolder = values[2];
        terms.DOB = values[3];
        terms.gender = values[4];
        terms.phone = values[5];
        terms.email = values[6];
        terms.emiratesid = values[7];
        terms.eidexpiry = values[8];
        terms.nationality = values[9];
        terms.companyname = values[10];
        terms.designation = values[11];
        terms.aturnover = Double.parseDouble(values[12]);
        terms.workaddress = values[13];
        terms.IBANn = values[14];
        terms.accountNumber = values[15];
        terms.balance = Double.parseDouble(values[16]);
        terms.points = Integer.parseInt(values[17]);
        terms.pused = values[18].equals("1") ? 1 : 0;
        terms.currentAmount = Double.parseDouble(values[19]);
        terms.t = convertStringToArrayList(values[20]);
        admin a = new admin();
        terms.b = a.convertStringToArrayList(values[21]);
        terms.fdRecords = FixedDeposit.convertStringToArrayList(values[22]);
        terms.l = LoanApplication.convertStringtoArrayList(values[23]);
        terms.enabled = values[24].equals("true") ? true : false;
        terms.targetAmount = Double.parseDouble(values[25]);
    }

    public static String convertArrayListToString(ArrayList<transactions> list) 
    {
        StringBuilder sb = new StringBuilder();
        if (list.isEmpty())
        return "New:0.0:0.0\\";
        for (transactions num : list) 
        {
            String num1 = num.gettype() + ":" + num.getamount() + ":" + num.getbalance();
            sb.append(num1).append("\\");
        }
        return sb.toString();
    }

    public static ArrayList<transactions> convertStringToArrayList(String s) 
    {
        if (s.equals("New:0.0:0.0\\"))
        {
            return new ArrayList<transactions>();
        }
        ArrayList<transactions> list = new ArrayList<transactions>();
        String[] arr = s.split("\\\\");
        for (String num : arr) 
        {
            String[] arr1 = num.split(":");
            list.add(new transactions(arr1[0], Double.parseDouble(arr1[1]), Double.parseDouble(arr1[2])));
        }
        return list;
    }

    public void change(Scanner scan)
    {
        System.out.print("Username: ");
        String un = scan.next();
        String pw;
        while (true)
        {
            System.out.println("Password criteria: \n1. Between 8-15 characters \n2. At least one uppercase letter \n3. At least one numeric character");
            System.out.print("Password: ");
            String p = scan.next();
            if (Signup.isValidPassword(p))
            {
                System.out.print("Confirm Password: ");
                String cp = scan.next();
                if (p.equals(cp))
                {
                System.out.println("Password confirmed successfully");
                pw = p;
                break;
                }
                else
                {
                    System.out.println("Passwords do not match. Please try again");
                }
            }
            else
            {
                System.out.println("Password does not meet the criteria. Please try again");
            }
        }
    }

    public void profile()
    {
        System.out.println("Your profile");
        System.out.println("\nPersonal details ");
        System.out.println("Name: " + terms.accountHolder);
        System.out.println("Date of Birth: " + terms.DOB);
        System.out.println("Gender: " + terms.gender);
        System.out.println("Emirates ID: " + terms.emiratesid);
        System.out.println("ID Expiry Date: " + terms.eidexpiry);
        System.out.println("Nationality: " + terms.nationality);
        System.out.println("\nContact details");
        System.out.println("Phone number: " + terms.phone);
        System.out.println("Email ID: " + terms.email);
        System.out.println("\nEmployment details");
        System.out.println("Company Name: " + terms.companyname);
        System.out.println("Designation: " + terms.designation);
        System.out.println("Annual Turnover: " + terms.aturnover);
        System.out.println("Work Address: " + terms.workaddress);
        System.out.println("\nBank details");
        System.out.println("IBAN: " + terms.IBANn);
        System.out.println("Account Number: " + terms.accountNumber);
        System.out.println("Balance: " + terms.balance);
        System.out.println("Points: " + terms.points);
        System.out.println("Current Amount Saved in Goal: " + terms.currentAmount);
    }

    public void logout()
    {
        try 
        {
            List<String []> csvData = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader("data.csv")))
            {
                String line;
                while ((line = br.readLine()) != null)
                {
                    csvData.add(line.split(","));
                }
            }

            boolean updated = false;

            for (String [] row : csvData)
            {
                if (row[0].equals(terms.un))
                {
                    row[0] = terms.un;
                    row[1] = terms.pw;
                    row[2] = terms.accountHolder;
                    row[3] = terms.DOB;
                    row[4] = terms.gender;
                    row[5] = terms.phone;
                    row[6] = terms.email;
                    row[7] = terms.emiratesid;
                    row[8] = terms.eidexpiry;
                    row[9] = terms.nationality;
                    row[10] = terms.companyname;
                    row[11] = terms.designation;
                    row[12] = Double.toString(terms.aturnover);
                    row[13] = terms.workaddress;
                    row[14] = terms.IBANn;
                    row[15] = terms.accountNumber;
                    row[16] = Double.toString(terms.balance);
                    row[17] = String.valueOf(terms.points);
                    row[18] = String.valueOf(terms.pused);
                    row[19] = Double.toString(terms.currentAmount);
                    row[20] = convertArrayListToString(terms.t);
                    row[21] = admin.convertArrayListToString(terms.b);
                    row[22] = FixedDeposit.convertArrayListToString(terms.fdRecords);
                    row[23] = LoanApplication.convertArrayListToString(terms.l);
                    row[24] = terms.enabled ? "true" : "false";
                    row[25] = Double.toString(terms.targetAmount);
                    updated = true;
                    break;
                }
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter("data.csv")))
            {
                for (String [] row : csvData)
                {
                    bw.write(String.join(",",row));
                    bw.newLine();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

    
    


class Signup
{
    public void details()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("------------------------------------------------------");
        System.out.println("|                     Sign Up                        |");
        System.out.println("------------------------------------------------------");
        System.out.println("\nGet Ready for the ultimate digital banking experience.");
        System.out.println("\nWhat are the advantages: \n 1.Easy and instant online application. \n 2.No Branch visits \n 3.Credit cards delivered at your doorstep.");
        System.out.println("\nFew things are required: \n 1.An active UAE Mobile number and an email ID \n 2.A valid emirates ID and a passport copy.");

        System.out.println("\n Please enter the following details: ");
        System.out.println("\nPersonal Details");
        System.out.print("Enter your full name: ");
        String fn = scan.nextLine();
        System.out.print("Enter the Date of Birth (DD/MM/YYYY):  ");
        String DOB = scan.nextLine();
        System.out.print("Enter the Gender (F/M): ");
        String g = scan.nextLine();

        System.out.println("\nContact Information");
        String pn;
        while (true)
        {
            System.out.print("Enter the phone number: ");
            String pn1 = scan.nextLine();
            if (isValidPhoneNumber(pn1))
            {
                pn = pn1;
                break;
            }
            else 
            {
                System.out.println("Phone number is Invalid. Please enter an UAE number");
            }
        }
        String email;
        
        while (true)
        {
            System.out.print("Enter the email ID: ");
            String eid = scan.nextLine();
            System.out.print("Please confirm the email ID: ");
            String ceid = scan.nextLine();
            if (eid.equals(ceid))
            {
                System.out.println("Email ID confirmed successfully");
                email = eid;
                break;
            }
            else
            {
                System.out.println("Email IDs do not match. Please try again");
            }
        }

        System.out.println("\nEmirates ID details");
        System.out.print("Emirates ID number: ");
        String eidn = scan.nextLine();
        System.out.print("Expiry Date: ");
        String ed = scan.nextLine();
        System.out.print("Nationality:");
        String n = scan.nextLine();

        System.out.println("\n(NOTE: This will be your official name for the bank's record, including the name on your credit card. Please ensure your name matches with your passport to avoid delays in activating your account.)");

        System.out.print("Full name as per passport: ");
        String fnp = scan.nextLine();

        System.out.println("\nEmployment Details");
        System.out.print("Company Name: ");
        String cn = scan.nextLine();
        System.out.print("Designation: ");
        String d = scan.nextLine();
        System.out.print("Annual Turnover: ");
        double an = scan.nextDouble();
        String Space = scan.nextLine();
        System.out.print("Work address: ");
        String wa = scan.nextLine();

        
        System.out.println("\nCreate Username and Password");
        System.out.print("Username: ");
        String un = scan.next();
        String pw;
        while (true)
        {
            System.out.println("Password criteria: \n1. Between 8-15 characters \n2. At least one uppercase letter \n3. At least one numeric character");
            System.out.print("Password: ");
            String p = scan.next();
            if (isValidPassword(p))
            {
                System.out.print("Confirm Password: ");
                String cp = scan.next();
                if (p.equals(cp))
                {
                System.out.println("Password confirmed successfully");
                pw = p;
                break;
                }
                else
                {
                    System.out.println("Passwords do not match. Please try again");
                }
            }
            else
            {
                System.out.println("Password does not meet the criteria. Please try again");
            }
        }

        System.out.println("Congratulations! Your account has been created successfully.");

        int CheckDigits = generateCD();
        int Sort = generatesort();
        int AccountID = generateaccountID();

        String IBANn = "AE" + CheckDigits + "JKSSR" + Sort + AccountID;

        System.out.println("\nYour IBAN number is " + "AE" + CheckDigits + "JKSSR" + Sort + AccountID);
        System.out.println("\nYour account number is " + AccountID);

        terms.Person(fn, DOB, g, pn, email, eidn, ed, n, cn, d, an, wa, un, pw, IBANn, AccountID);
    }


    public static boolean isValidPassword(String password)
    {
        if (password.length() >= 8 && password.length() <= 15)
        {
            int uc = 0;
            for (int i = 0; i < password.length(); i++)
            {
                if (Character.isUpperCase(password.charAt(i)))
                {
                    uc = 1;
                    break;
                }
            }
            int num = 0;
            for (int i = 0; i < password.length(); i++)
            {
                if (Character.isDigit(password.charAt(i)))
                {
                    num = 1;
                    break;
                }
            }
            if (uc == 1 && num == 1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else return false;
    }

    private boolean isValidPhoneNumber(String phoneNumber) 
    {
        if (phoneNumber.length() == 14)
        {
            String s1 = phoneNumber.substring(0, 5);
            if (s1.equals("00971"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private int generateCD()
    {
        Random cd = new Random();
        return cd.nextInt(100);
    }

    private int generatesort()
    {
        Random sort = new Random();
        return sort.nextInt(1000000);
    }

    private int generateaccountID()
    {
        Random AID = new Random();
        return AID.nextInt(10000000);
    }
}

class Rewards
{   
    public void getPoints()
    {
        //terms.points = terms.t.size()/100;
        System.out.println("Points: " + terms.points);

    }

    public void Tier()
    {
        Scanner scan = new Scanner(System.in);
        if (terms.t.size() > 500)
        {
            System.out.println("Congratulations, you have won our exclusive GOLDEN Tier Status!!");
            if (terms.pused == 0)
            {
                System.out.println("1. 10% Cashback Reward \n2. Travel tickt to France for 2people \n3. Double my points \n4. You'll choose later on");
                System.out.print("Which of the following benefits would you like to redeem?: ");
                int reward = scan.nextInt();
                terms.pused = 1;
                if (reward == 1)
                {
                    double cashback = terms.points* 0.1f;
                    terms.balance += cashback;
                    System.out.println("Balance updated with 10% cashback");
                    System.out.println("Current balance: " + terms.balance);
                    terms.t.add(new transactions("CashBack", cashback, terms.balance));
                    terms.points -= 500;
                }
                else if (reward == 2)
                {
                    System.out.println("You have redeemed a travel ticket to France for 2 people");
                    terms.points -= 500;
                }
                else if (reward == 3)
                {
                    terms.points *= 2;
                    System.out.println("Points doubled");
                    System.out.println("Current points: " + terms.points);
                }
                else if (reward == 4)
                {
                    terms.pused = 0;
                    System.out.println("You will choose later on");
                }
                else 
                {
                    System.out.println("Invalid option, try again");
                }
            }
            else
            {
                System.out.println("Reward already redeemed. Try again next month");
            }
        }
        else if (terms.t.size() > 300)
        {
            System.out.println("Congratulations, you have won our exclusive SILVER Tier Status!!");
            if (terms.pused == 0)
            {
                System.out.println("1. 5% Cashback Reward \n2. Travel tickt to any city in India \n3. Add 1000 points \n4. You'll choose later on");
                System.out.print("Which of the following benefits would you like to redeem?; ");
                int reward = scan.nextInt();
                terms.pused = 1;
                if (reward == 1)
                {
                    double cashback = terms.points* 0.05f;
                    terms.balance += cashback;
                    System.out.println("Balance updated with 5% cashback");
                    System.out.println("Current balance: " + terms.balance);
                    terms.t.add(new transactions("CashBack", cashback, terms.balance));
                    terms.points -= 300;
                }
                else if (reward == 2)
                {
                    System.out.println("You have redeemed a travel ticket to any city in India");
                    terms.points -= 300;
                }
                else if (reward == 3)
                {
                    terms.points += 1000;
                    System.out.println("1000 points added");
                    System.out.println("Current points: " + terms.points);
                }
                else if (reward == 4)
                {
                    terms.pused = 0;
                    System.out.println("You will choose later on");
                }
                else 
                {
                    System.out.println("Invalid option, try again");
                }
            }
            else
            {
                System.out.println("Reward already redeemed. Try again next month");
            }
        }
        else if (terms.t.size() > 100)
        {
            System.out.println("Congratulations, you have won our exclusive BRONZE Tier Status!!");
            if (terms.pused == 0)
            {
                System.out.println("1. 2% Cashback Reward \n2. VIP Lounge Access for 2 people \n3. Get 500 reward points \n4. You'll choose later on");
                System.out.print("Which of the following benefits would you like to redeem?: ");
                int reward = scan.nextInt();
                terms.pused = 1;
                if (reward == 1)
                {
                    double cashback = terms.points* 0.02f;
                    terms.balance += cashback;
                    System.out.println("Balance updated with 2% cashback");
                    System.out.println("Current balance: " + terms.balance);
                    terms.t.add(new transactions("CashBack", cashback, terms.balance));
                    terms.points -= 100;
                }
                else if (reward == 2)
                {
                    System.out.println("You have redeemed VIP Lounge Access for 2 people");
                    terms.points -= 100;
                }
                else if (reward == 3)
                {
                    terms.points += 500;
                    System.out.println("500 points added");
                    System.out.println("Current points: " + terms.points);
                }
                else if (reward == 4)
                {
                    terms.pused = 0;
                    System.out.println("You will choose later on");
                }
                else 
                {
                    System.out.println("Invalid option, try again");
                }
            }
            else
            {
                System.out.println("Reward already redeemed. Try again next month");
            }
        }
        else
        System.out.println((100 - terms.points) + " more to go to get your first tier status");
    }

    public void Points()
    {
        Scanner scan = new Scanner(System.in);
        if (terms.points < 300)
        {
            System.out.println("Insufficient points. \n" + (300 - terms.points) + " more to go to start availing rewards");
            return;
        }
        while (true)
        {
            System.out.println("\nCurrently available offers:");
            System.out.println("1. Enjoy a lunch at Atlantis Dubai for 1000 points  \n2. Shopping Voucher for AED100 at Kalyan Jewelers for 300 points \n3. Burj Al Arab visit for 1000 points \n4. Ferrari World ticket for 2 people for 2000 points \n5. You'll choose later on");
            System.out.print("Which of the following benefits would you like to redeem?: ");
            int select = scan.nextInt();
            if (select == 1 && terms.points >= 1000)
            {
                terms.points -= 1000;
                System.out.println("Coupon remdeemed. Enjoy your lunch");
                System.out.println("Current points: " + terms.points);
            }
            else if (select == 2 && terms.points >= 300)
            {
                terms.points -= 300;
                System.out.println("Coupon redeemed. Enjoy shopping");
                System.out.println("Current points: " + terms.points);
            }
            else if (select == 3 && terms.points >= 1000)
            {
                terms.points -= 1000;
                System.out.println("Coupon redeemed. Enjoy visiting Burj Al Arab");
                System.out.println("Current points: " + terms.points);
            }
            else if (select == 4 && terms.points >= 2000)
            {
                terms.points -= 2000;
                System.out.println("Coupon redeemed. Enjoy visiting Ferrari World");
                System.out.println("Current points: " + terms.points);
            }
            else if (select == 5)
            {
                System.out.println("You will choose later on");
                break;
            }
            else
            {
                System.out.println("Invalid option, try again");
            }
        }
    }
}


class Goal
{
    private String goalName;
    private double targetAmount;
    private int monthsToSave;

    public Goal(String goalName, double targetAmount, int monthsToSave, double currentAmount)
    {
        this.goalName = goalName;
        //terms.targetAmount = terms.targetAmount;
        this.monthsToSave = monthsToSave;
        terms.currentAmount += currentAmount;
    }

    public String getGoalName()
    {
        return goalName;
    }

    public double getTargetAmount()
    {
        return terms.targetAmount;
    }

    public double getCurrentAmount()
    {
        return terms.currentAmount;
    }

    public int getMonthsToSave()
    {
        return monthsToSave;
    }

    public int display()
    {
        if (terms.targetAmount == 0)
        return 0;
        System.out.println("Target Amount: " + terms.targetAmount + "\nCurrent Amount Saved: " + terms.currentAmount);
        //System.out.println("Goal: " + goalName + "\nTarget Amount: " + targetAmount + "\nCurrent Amount Saved: " + terms.currentAmount + "\nMonths to Save: " + monthsToSave);
        return 1;
    }

    public void addAmount(double amt)
    {
        if (amt <= 0)
        {
            System.out.println("Amount should be positive");
            return;
        }
        else if (terms.balance < amt)
        {
            System.out.println("Insufficient funds");
            return;
        }
        else if (terms.currentAmount + amt > terms.targetAmount)
        {
            System.out.println("Cannot save more than your target. Please try again");
            return;
        }
        else
        {
            terms.currentAmount += amt;
            terms.balance -= amt;
            terms.points += (amt * 0.01);
            terms.t.add(new transactions("Goal Deposit", amt, terms.balance));
            terms.points += (amt * 0.01);
            System.out.println("Amount added successfully.\nNew saved amount: " + terms.currentAmount);
        }
    }

    public void withdrawAmount(double amt)
    {
        if (amt <= 0)
        {
            System.out.println("Amount should be positive");
            return;
        }
        else if (amt > terms.currentAmount)
        {
            System.out.println("Not enough saved amount in the goal to withdraw.");
            return;
        }
        else
        {
            terms.currentAmount -= amt;
            terms.balance += amt;
            terms.t.add(new transactions("Goal Withdrawal", amt, terms.balance));
            System.out.println("Amount withdrawn successfully.\nNew saved amount: " + terms.currentAmount);
        }
    }

    public void extendtime(int months)
    {
        this.monthsToSave += months;
    }

}


class terms
{
    static String accountNumber; //accountid
    static String accountHolder; //name
    static double balance;
    static int points;
    static int pused;
    static double currentAmount = 0; //goal management
    static double targetAmount = 0;
    static ArrayList<transactions> t = new ArrayList<transactions>();
    static String DOB;
    static String gender;
    static String phone;
    static String email;
    static String emiratesid;
    static String eidexpiry;
    static String nationality;
    static String companyname;
    static String designation;
    static double aturnover;
    static String workaddress;
    static String un;
    static String pw;
    static String IBANn;
    static ArrayList<booking> b = new ArrayList<booking>();
    static ArrayList<FixedDeposit> fdRecords = new ArrayList<FixedDeposit>();
    static ArrayList<LoanApplication> l = new ArrayList<LoanApplication>();
    static boolean enabled = true;


    static void Person (String n, String dob, String g, String pn, String em, String eid, String idexpiry, String ntlty, String cn, String d, double an, String wa, String uname, String pword, String iban, int AID)
    {
        set_default();
        try (FileWriter writer = new FileWriter("data.csv",true))
        {
           // writer.append("UserName, Password, Name, DOB, Gender, Phone Number, Email ID, Emirates ID, ID Expiry Date, Nationality, Company Name, Designation, Annual Turnover, Work Address, IBAN, Account ID, Balance, Points, Points Used, Current Amount Saved in Goal, Transactions, Bookings, Fixed Deposits, Loans, Enabled, Target Amount\n");
            writer.append(uname + "," + pword + "," + n + "," + dob + "," + g + "," + pn + "," + em + "," + eid + "," + idexpiry + "," + ntlty + "," + cn + "," + d + "," + an + "," + wa + "," + iban + "," + AID + ","+ balance + "," + points + "," + pused + "," + currentAmount + ","  +  login.convertArrayListToString(t) + "," + admin.convertArrayListToString(b) + "," + FixedDeposit.convertArrayListToString(fdRecords) + "," + LoanApplication.convertArrayListToString(l)+ "," + "true" +  "," + targetAmount + "\n");
        }
        catch (IOException e) 
        {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
        }
    }

    
    static void set_default()
    {
        balance = 0;
        points = 0;
        pused = 0;
        currentAmount = 0;
        t.add(new transactions("New", 0, 0));

    }
}

class Transfer 
{
    public void transferFunds()
    {
        if (terms.balance == 0)
        {
            System.out.println("Insufficient funds for transfer.");
            return;
        }
        ArrayList<String []> csvData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data.csv")))
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                String[] values = line.split(",");
                csvData.add(values);
            }    
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(System.in);
        ArrayList<transactions> tr = new ArrayList<transactions>(); //receiver transactions
        double balancer = 0; //receiver balance
        int found = 0;
        String receiverAccountID = "";
        while (true)
        {
            if (found == 0)
            {
                System.out.print("Enter the receiver's account number(0: Exit): ");
                receiverAccountID = scan.next();
                for (int i = 0; i < csvData.size(); i++)
                {
                    if (csvData.get(i)[15].equals(receiverAccountID))
                    {
                        balancer = Double.parseDouble(csvData.get(i)[16]);
                        tr = login.convertStringToArrayList(csvData.get(i)[20]);
                        found = 1;
                        break;
                    }
                }
                if (receiverAccountID.equals("0"))
                break;
                if (found == 0)
                {
                    System.out.println("Receiver account not found.");
                    continue;
                }
            }
            System.out.print("Enter the amount to transfer: ");
            double amount = scan.nextDouble();
            if (terms.balance < amount)
            {
                System.out.println("Insufficient funds for transfer.");
                continue;
            }
            else
            {
                terms.balance -= amount;
                balancer += amount;
                terms.t.add(new transactions("Transfered", amount, terms.balance));
                tr.add(new transactions("Received", amount, balancer));
                terms.points += (amount * 0.02);
                for (int i = 0; i < csvData.size(); i++)
                {
                    if (csvData.get(i)[15].equals(receiverAccountID))
                    {
                        csvData.get(i)[16] = String.valueOf(balancer);
                        csvData.get(i)[20] = login.convertArrayListToString(tr);
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data.csv")))
                        {
                            for (int j = 0; j < csvData.size(); j++)
                            {
                                writer.write(String.join(",", csvData.get(j)) + "\n");
                            }
                        }
                        catch (IOException e) 
                        {
                            e.printStackTrace();
                        }
                        System.out.println("Transfer successful.");
                        break;
                    }
                }
                System.out.println("Transfer successful.");
                break;
            }
        }
    }
}

class transactions
{
    double a,b;
    String type;
    
    transactions(String t, double a, double b)
    {
        this.type = t;
        this.a = a;
        this.b = b;
    }
    
    String gettype()
    {
        return type;
    }
    
    double getamount()
    {
        return a;
    }
    
    double getbalance()
    {
        return b;
    }
}


class deposits_withdrawals
{   
    deposits_withdrawals(double b)
    {
        terms.balance = b;
    }
    public double ABU(double amount, String type) //Account balance update
    {
        if (type == "w")
        {
            String v = validation(amount);
            if (v.equals("yes"))
            {
                terms.balance -= amount;
                display();
                terms.t.add(new transactions("Withdrawal", amount, terms.balance));
                terms.points += (amount * 0.01);
            }
            else
            {
                System.out.println("Your balance is low");
            }
        }
        if (type == "d")
        {
            terms.balance += amount;
            terms.points += (amount * 0.02);
            display();
            terms.t.add(new transactions("Deposit", amount, terms.balance));
        }
        return terms.balance;
    }
    
    
    public String validation(double amount)
    {
        if (amount <= terms.balance)
        return "yes";
        else
        return "no";
    }
    public void display()
    {
        System.out.println("Current Balance is: " + terms.balance);
    }
    
    public void displaytransactions()
    {
        if(terms.t.size()>0)
        {
            System.out.println("Transaction Type : Amount : Balance");
            for(int i=0; i<terms.t.size(); i++)
            {
                System.out.println(terms.t.get(i).gettype() + " : " + terms.t.get(i).getamount() + " : " + terms.t.get(i).getbalance());
            }
        }
        else
        System.out.println("No transactions done yet");
    }
}


public class bank1
{
    public static void main(String [] args)
    {
        Scanner scan = new Scanner(System.in);
        Signup s = new Signup();
        while (true)
        {
            System.out.println("\n1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Admin");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int option1 = scan.nextInt();
            if (option1 == 1)
            {
                s.details();
                //sign up method/class
            }
            else if (option1 == 2)
            {
                login l = new login();

                int entered = l.enter();

                if (entered == 0)
                {
                    continue;
                }

                System.out.println("\nWelcome " + terms.accountHolder);
                Goal goal = new Goal("",0,0,0);
                Rewards r = new Rewards();
                while (true)
                {
                    System.out.println("\n1. View Profile");
                    System.out.println("2. Manage Appointments");
                    System.out.println("3. Deposits and withdrawals");
                    System.out.println("4. Fund Transfer");
                    System.out.println("5. Transaction History");
                    System.out.println("6. Fixed deposit");
                    System.out.println("7. Loan management");
                    System.out.println("8. Financial goals and saving plans");
                    System.out.println("9. Rewards and loyalty programs");
                    System.out.println("10. Logout\n");
                    
                    System.out.print("Enter your choice: ");
                    
                    int option2 = scan.nextInt();
                    
                    if (option2 == 1)
                    {
                        l.profile();
                        //method from jessica
                    }
                    else if (option2 == 2)
                    {
                        AppointmentSystem system = new AppointmentSystem();
                        while (true)
                        {
                            System.out.println("\n1. Make a new appointment \n2. Cancel an already existing appointment \n3. View your appointments \n4. Exit");
                            System.out.print("Enter your choice: ");
                            int option3 = scan.nextInt();
                            if (option3 == 1)
                            {
                                system.makeappointment(scan);
                            }
                            else if (option3 == 2)
                            {
                                system.cancelappointment(scan);
                            }
                            else if (option3 == 3)
                            {
                                system.viewappointments(scan);
                            }
                            else if (option3 == 4)
                            {
                                break;
                            }
                            else
                            System.out.println("Wrong option selected");
                        }

                        //method from jessica
                    }
                    else if (option2 == 3)
                    {
                        deposits_withdrawals dw = new deposits_withdrawals(terms.balance); 
                        while (true)
                        {
                            System.out.println("\n1. Withdraw amount");
                            System.out.println("2. Deposit amount");
                            System.out.println("3. Cancel transaction");
                            System.out.print("Enter your choice: ");
                            int option3 = scan.nextInt();
                            if (option3 == 3)
                            break;
                            System.out.print("Amount: ");
                            int amount = scan.nextInt();
                            if (option3 == 1)
                            {
                                terms.balance = dw.ABU(amount, "w");
                            }
                            else if (option3 == 2)
                            {
                                terms.balance = dw.ABU(amount, "d");
                            }
                            else
                            System.out.println("Wrong option selected");
                        }
                        //my method
                    }
                    else if (option2 == 4)  //fund transfer - change required
                    {
                        Transfer transfer = new Transfer();
                        transfer.transferFunds();
                       //rahul's method
                    }
                    else if(option2 == 5)
                    {
                        deposits_withdrawals dw = new deposits_withdrawals(terms.balance);
                        dw.displaytransactions();
                        //rahul
                    }
                    else if(option2 == 6)
                    {
                        FDManagementSystem fd = new FDManagementSystem();
                        boolean flow = true;
                        while (flow)
                        {
                            System.out.println("1. Create an FD \n2. View your FDs \n3. FD withdrawal \n4. Exit");
                            System.out.print("Enter your choice: ");
                            int option3 = scan.nextInt();
                            switch (option3) {
                                case 1:
                                    fd.createFD();
                                    break;
                                case 2:
                                    fd.viewFD();
                                    break;
                                case 3:
                                    fd.Withdrawal(scan);
                                    break;
                                case 4:
                                    System.out.println("Exiting the FD Management System. Goodbye!");
                                    flow = false;
                                    break;
                                default:
                                    System.out.println("Invalid option selected. Please try again.");
                            }
                        }
                        //shreya
                    }
                    else if(option2 == 7)
                    {
                        LoanManagementSystem loan = new LoanManagementSystem();
                        while (true)
                        {
                            System.out.println("\n1. Take a loan");
                            System.out.println("2. View your previous loan applications");
                            System.out.println("3. Repay a loan");
                            System.out.println("4. Exit\n");
                            System.out.print("Select your choice: ");
                            int option3 = scan.nextInt();
                            if (option3 == 1)
                            {
                                loan.takeloan(scan);
                            }
                            else if (option3 == 2)
                            {
                                loan.viewloan();
                            }
                            else if (option3 == 3)
                            {
                                loan.LoanRepayment(scan);
                            }
                            else if (option3 == 4)
                            {
                                System.out.println("Exiting the Loan Management System. Goodbye!");
                                break;
                            }
                            else
                            {
                                System.out.println("Invalid option selected. Please try again.");
                            }
                        }
                        //shreya
                    }
                    else if(option2 == 8)
                    {
                        while (true)
                        {
                            System.out.println("\n1. Create a Savings Goal");
                            System.out.println("2. View your previous Savings Goal");
                            System.out.println("3. Add Balance to your savings");
                            System.out.println("4. Withdraw from you Savings");
                            System.out.println("5. Extend the time limit");
                            System.out.println("6. Exit\n");
                            System.out.print("Select your choice: ");
                            int option3 = scan.nextInt();

                            if (option3 == 1)
                            {
                                System.out.println("1. Short-term Plan \n2. Medium-term Plan \n3. Long-term Plan \n5. Cancel");
                                System.out.print("Choose the Savings Plan you wish to opt for: ");
                                int plan = scan.nextInt();
                                if (plan == 1)
                                {
                                    System.out.print("How many Months do you want to save for (Range: 1-6): ");
                                    int months = scan.nextInt();
                                    System.out.print("Target Amount: ");
                                    terms.targetAmount += scan.nextDouble();
                                    goal = new Goal("Short-term", terms.targetAmount, months, 0);
                                }
                                else if (plan == 2)
                                {
                                    System.out.print("How many Months do you want to save for (Range: 7-12): ");
                                    int months = scan.nextInt();
                                    System.out.print("Target Amount: ");
                                    terms.targetAmount += scan.nextDouble();
                                    goal = new Goal("Medium-term", terms.targetAmount, months, 0);
                                }
                                else if (plan == 3)
                                {
                                    System.out.print("How many Months do you want to save for (Range: 13-24): ");
                                    int months = scan.nextInt();
                                    System.out.print("Target Amount: ");
                                    terms.targetAmount += scan.nextDouble();
                                    goal = new Goal("Long-term", terms.targetAmount, months, 0);
                                }
                                else if (plan == 4)
                                {
                                    break;
                                }
                            }

                            else if (option3 == 2)
                            {
                                int r1 = goal.display();
                                if (r1 == 0)
                                System.out.println("No records available");
                            }
                            else if (option3 == 3)
                            {
                                System.out.print("Enter the amount to add to your goal: ");
                                double amt = scan.nextDouble();
                                goal.addAmount(amt);
                            }
                            else if (option3 == 4)
                            {
                                System.out.print("Enter the amount to withdraw from your goal: ");
                                double amt = scan.nextDouble();
                                goal.withdrawAmount(amt);
                            }
                            else if (option3 == 5)
                            {
                                System.out.print("Enter the amount of months you want to extend it by: ");
                                int months = scan.nextInt();
                                goal.extendtime(months);
                            }
                            else if (option3 == 6)
                            break;
                            else
                            System.out.println("Wrong option selected");

                        }
                        
                        //Goal goal = new Goal("Short-term", 5)
                        //shahna
                    }
                    else if(option2 == 9)
                    {
                        while(true)
                        {
                            System.out.println("\n1. Check how many points I have");
                            System.out.println("2. My Tier status and rewards");
                            System.out.println("3. My rewards");
                            System.out.println("4. Exit\n");
                            System.out.print("What do you want to do: ");
                            int option3 = scan.nextInt();

                            if (option3 == 1)
                            {
                                r.getPoints();
                            }

                            else if (option3 == 2)
                            {
                                r.Tier();
                            }
                        
                            else if (option3 == 3)
                            {
                                r.Points();
                            }

                            else if (option3 == 4)
                            {
                                break;
                            }

                            else 
                            {
                                System.out.println("Invalid option selected");
                            }
                        }
                        //shahna
                    }
                    else if(option2 == 10)
                    {
                        l.logout();
                        System.out.println("Logged out successfully");
                        break;
                    }
                    else
                    {
                        System.out.println("Invalid option selected");
                    }
                }
                
            }
            else if (option1 == 3)
            {
                admin a = new admin();

        while (true)
        {
            System.out.println("\n1. Login");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int select = scan.nextInt();

            if (select == 1)
            {
                if (a.enter() == 0)
                {
                    System.out.println("Login failed.");
                    continue;
                }
                while (true)
                {
                    System.out.println("\n1. Create new admin");
                    System.out.println("2. View all admins");
                    System.out.println("3. View bookings");
                    System.out.println("4. Remove a booking");
                    System.out.println("5. View your customers");
                    System.out.println("6. Enable/Disable a customer");
                    System.out.println("7. Logout\n");
                    System.out.print("Enter your choice: ");
                    int option = scan.nextInt();

                    if (option == 1)
                    {
                        System.out.print("\nEnter Name: ");
                        String n = scan.next();
                        System.out.print("Enter phone number: ");
                        String pn = scan.next();
                        System.out.print("Enter email ID: ");
                        String em = scan.next();
                        System.out.print("Enter position: ");
                        String p = scan.next();
                        System.out.print("Enter username: ");
                        String un = scan.next();
                        System.out.print("Enter password: ");
                        String pw = scan.next();
                        a.createadmin(un, pw, n, pn, em, p);
                    }
                    else if (option == 2)
                    {
                        a.viewadmin();
                    }
                    else if (option == 3)
                    {
                        a.viewbookings();
                    }
                    else if (option == 4)
                    {
                        a.removebooking(scan);
                    }
                    else if (option == 5)
                    {
                        a.viewcustomer();
                    }
                    else if (option == 6)
                    {
                        a.enabledisablecus(scan);
                    }
                    else if (option == 7)
                    {
                        a.adminlogout();
                        break;
                    }
                    else
                    System.out.println("Wrong option selected");
                }
            }

            else if (select == 2)
            {
                break;
            }
            else
            System.out.println("Wrong option selected");
        }
                //admin method
            }
            else if (option1 == 4)
            {
                break;
            }
            else
            System.out.println("Invalid option selected");
            
            
        }
        
        
    }
}
