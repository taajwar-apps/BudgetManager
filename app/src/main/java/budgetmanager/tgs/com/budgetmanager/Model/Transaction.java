package budgetmanager.tgs.com.budgetmanager.Model;

import android.media.Image;

public class Transaction {

    String transactionCategory;//category ID
    String transactionCategoryGroup; //category group ID
    String transactionAccount; //account ID
    float transactionAmount;
    Image transactionCategoryImage;
    Image transactionAccountImage;

    public enum TRANSACTION_TYPE{EXPENSE, INCOME, TRANSFER}

    TRANSACTION_TYPE transactionType;

    class Date{
        public String date;
        public String day;
        public String month;
        public String year;
    }

    Date transactionDate;

    public void setDate(){

    }

    public String getTransactionCategory(){
        return transactionCategory;
    }
    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionCategoryGroup(){
        return transactionCategory;
    }
    public void setTransactionCategoryGroup(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public String getTransactionAccount() {
        return transactionAccount;
    }

    public void setTransactionAccount(String transactionAccount) {
        this.transactionAccount = transactionAccount;
    }

    public float getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(float transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Image getTransactionAccountImage() {
        return transactionAccountImage;
    }

    public void setTransactionAccountImage(Image transactionAccountImage) {
        this.transactionAccountImage = transactionAccountImage;
    }

    public Image getTransactionCategoryImage() {
        return transactionCategoryImage;
    }

    public void setTransactionCategoryImage(Image transactionCategoryImage) {
        this.transactionCategoryImage = transactionCategoryImage;
    }

    public TRANSACTION_TYPE getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TRANSACTION_TYPE transactionType) {
        this.transactionType = transactionType;
    }
}
