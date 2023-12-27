package org.n9ne.auth.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001d\u001a\u00020\u001eJ\u0006\u0010\u001f\u001a\u00020\u001eJ\u0018\u0010 \u001a\u00020\u001e2\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H\u0002J\u0018\u0010%\u001a\u00020\f2\u0006\u0010&\u001a\u00020\u00062\u0006\u0010\'\u001a\u00020\u0006H\u0002J\u001e\u0010(\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\u00062\u0006\u0010\'\u001a\u00020\u00062\u0006\u0010!\u001a\u00020\"J\u0006\u0010)\u001a\u00020\u001eJ\u0006\u0010*\u001a\u00020\u001eR\u001d\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\bR\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\bR\u001d\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\bR\u001a\u0010\u0010\u001a\u00020\u0011X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001c\u00a8\u0006+"}, d2 = {"Lorg/n9ne/auth/viewModel/LoginViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "ldError", "Landroidx/lifecycle/MutableLiveData;", "Lorg/n9ne/common/Event;", "", "getLdError", "()Landroidx/lifecycle/MutableLiveData;", "ldName", "getLdName", "ldPasswordClick", "", "getLdPasswordClick", "ldToken", "getLdToken", "navigator", "Lorg/n9ne/common/interfaces/Navigator;", "getNavigator", "()Lorg/n9ne/common/interfaces/Navigator;", "setNavigator", "(Lorg/n9ne/common/interfaces/Navigator;)V", "passwordIsVisible", "repo", "Lorg/n9ne/auth/auth/AuthRepo;", "getRepo", "()Lorg/n9ne/auth/auth/AuthRepo;", "setRepo", "(Lorg/n9ne/auth/auth/AuthRepo;)V", "forgetPasswordClick", "", "googleClick", "initDatabase", "context", "Landroid/content/Context;", "data", "Lorg/n9ne/common/model/User;", "isDataValid", "email", "password", "login", "passwordClick", "registerClick", "auth_debug"})
public final class LoginViewModel extends androidx.lifecycle.ViewModel {
    private boolean passwordIsVisible = false;
    public org.n9ne.common.interfaces.Navigator navigator;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> ldPasswordClick = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldName = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldToken = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldError = null;
    @org.jetbrains.annotations.Nullable
    private org.n9ne.auth.auth.AuthRepo repo;
    
    public LoginViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.n9ne.common.interfaces.Navigator getNavigator() {
        return null;
    }
    
    public final void setNavigator(@org.jetbrains.annotations.NotNull
    org.n9ne.common.interfaces.Navigator p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getLdPasswordClick() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> getLdName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> getLdToken() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> getLdError() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.n9ne.auth.auth.AuthRepo getRepo() {
        return null;
    }
    
    public final void setRepo(@org.jetbrains.annotations.Nullable
    org.n9ne.auth.auth.AuthRepo p0) {
    }
    
    public final void passwordClick() {
    }
    
    public final void login(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    private final boolean isDataValid(java.lang.String email, java.lang.String password) {
        return false;
    }
    
    private final void initDatabase(android.content.Context context, org.n9ne.common.model.User data) {
    }
    
    public final void googleClick() {
    }
    
    public final void registerClick() {
    }
    
    public final void forgetPasswordClick() {
    }
}