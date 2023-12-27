package org.n9ne.auth.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\'\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\fJ/\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00110\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012J7\u0010\u0013\u001a\u00020\u00062\u0006\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00060\nH\u0096@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0016"}, d2 = {"Lorg/n9ne/auth/auth/AuthRepoImpl;", "Lorg/n9ne/auth/auth/AuthRepo;", "client", "Lorg/n9ne/common/source/network/Client;", "(Lorg/n9ne/common/source/network/Client;)V", "completeProfile", "", "data", "Lorg/n9ne/common/source/objects/Auth$CompleteProfile;", "callback", "Lorg/n9ne/common/RepoCallback;", "Lorg/n9ne/common/model/CompleteProfileResult;", "(Lorg/n9ne/common/source/objects/Auth$CompleteProfile;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "email", "", "password", "Lorg/n9ne/common/model/LoginResult;", "(Ljava/lang/String;Ljava/lang/String;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "name", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "auth_debug"})
public final class AuthRepoImpl implements org.n9ne.auth.auth.AuthRepo {
    @org.jetbrains.annotations.NotNull
    private final org.n9ne.common.source.network.Client client = null;
    
    public AuthRepoImpl(@org.jetbrains.annotations.NotNull
    org.n9ne.common.source.network.Client client) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object login(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<org.n9ne.common.model.LoginResult> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object register(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<kotlin.Unit> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.Nullable
    public java.lang.Object completeProfile(@org.jetbrains.annotations.NotNull
    org.n9ne.common.source.objects.Auth.CompleteProfile data, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<org.n9ne.common.model.CompleteProfileResult> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}