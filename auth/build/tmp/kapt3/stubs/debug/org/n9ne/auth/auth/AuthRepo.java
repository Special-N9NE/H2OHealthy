package org.n9ne.auth.auth;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001J\'\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\tJ/\u0010\n\u001a\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000fJ7\u0010\u0010\u001a\u00020\u00032\u0006\u0010\u0011\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007H\u00a6@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0013"}, d2 = {"Lorg/n9ne/auth/auth/AuthRepo;", "", "completeProfile", "", "data", "Lorg/n9ne/common/source/objects/Auth$CompleteProfile;", "callback", "Lorg/n9ne/common/RepoCallback;", "Lorg/n9ne/common/model/CompleteProfileResult;", "(Lorg/n9ne/common/source/objects/Auth$CompleteProfile;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "email", "", "password", "Lorg/n9ne/common/model/LoginResult;", "(Ljava/lang/String;Ljava/lang/String;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "register", "name", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lorg/n9ne/common/RepoCallback;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "auth_debug"})
public abstract interface AuthRepo {
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object login(@org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<org.n9ne.common.model.LoginResult> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object register(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<kotlin.Unit> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object completeProfile(@org.jetbrains.annotations.NotNull
    org.n9ne.common.source.objects.Auth.CompleteProfile data, @org.jetbrains.annotations.NotNull
    org.n9ne.common.RepoCallback<org.n9ne.common.model.CompleteProfileResult> callback, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}