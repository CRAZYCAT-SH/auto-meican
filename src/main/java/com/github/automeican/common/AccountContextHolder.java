package com.github.automeican.common;

public class AccountContextHolder {

    private static final ThreadLocal<String> account = new ThreadLocal<>();

    public static void setAccount(String account) {
        AccountContextHolder.account.set(account);
    }

    public static String getAccount() {
        return account.get();
    }

    public static void clear() {
        account.remove();
    }
}
