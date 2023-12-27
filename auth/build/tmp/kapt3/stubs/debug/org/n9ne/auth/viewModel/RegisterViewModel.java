package org.n9ne.auth.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001c\u001a\u00020\rJ \u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u0006H\u0002J\u0006\u0010!\u001a\u00020\rJ\u0006\u0010\"\u001a\u00020\rJ\u001e\u0010#\u001a\u00020\r2\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u001f\u001a\u00020\u00062\u0006\u0010 \u001a\u00020\u0006R\u001d\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\bR\u001d\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\bR\u001a\u0010\u000f\u001a\u00020\u0010X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u0019\"\u0004\b\u001a\u0010\u001b\u00a8\u0006$"}, d2 = {"Lorg/n9ne/auth/viewModel/RegisterViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "ldError", "Landroidx/lifecycle/MutableLiveData;", "Lorg/n9ne/common/Event;", "", "getLdError", "()Landroidx/lifecycle/MutableLiveData;", "ldPasswordClick", "", "getLdPasswordClick", "ldRegister", "", "getLdRegister", "navigator", "Lorg/n9ne/common/interfaces/Navigator;", "getNavigator", "()Lorg/n9ne/common/interfaces/Navigator;", "setNavigator", "(Lorg/n9ne/common/interfaces/Navigator;)V", "passwordIsVisible", "repo", "Lorg/n9ne/auth/auth/AuthRepo;", "getRepo", "()Lorg/n9ne/auth/auth/AuthRepo;", "setRepo", "(Lorg/n9ne/auth/auth/AuthRepo;)V", "googleClick", "isDataValid", "name", "email", "password", "loginClick", "passwordClick", "register", "auth_debug"})
public final class RegisterViewModel extends androidx.lifecycle.ViewModel {
    private boolean passwordIsVisible = false;
    public org.n9ne.common.interfaces.Navigator navigator;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> ldPasswordClick = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<kotlin.Unit>> ldRegister = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldError = null;
    @org.jetbrains.annotations.Nullable
    private org.n9ne.auth.auth.AuthRepo repo;
    
    public RegisterViewModel() {
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
    public final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<kotlin.Unit>> getLdRegister() {
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
    
    public final void loginClick() {
    }
    
    public final void googleClick() {
    }
    
    public final void register(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password) {
    }
    
    private final boolean isDataValid(java.lang.String name, java.lang.String email, java.lang.String password) {
        return false;
    }
}