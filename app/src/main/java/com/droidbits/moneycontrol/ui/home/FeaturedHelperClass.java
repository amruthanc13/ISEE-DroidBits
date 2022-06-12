package com.droidbits.moneycontrol.ui.home;

public class FeaturedHelperClass {

    private String title;
    private String expense;
    private String income;

    public FeaturedHelperClass(String title, String expense, String income) {
        this.title = title;
        this.expense = expense;
        this.income = income;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }
}
