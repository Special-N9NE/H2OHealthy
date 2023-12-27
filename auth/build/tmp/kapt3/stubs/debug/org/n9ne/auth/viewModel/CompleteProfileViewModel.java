package org.n9ne.auth.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002JN\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u00052\u0006\u0010$\u001a\u00020\u00052\u0006\u0010%\u001a\u00020\u00052\u0006\u0010&\u001a\u00020\u00052\u0006\u0010\'\u001a\u00020\u00052\u0006\u0010(\u001a\u00020\u00052\u0006\u0010)\u001a\u00020*J\u0006\u0010+\u001a\u00020\u0010J \u0010,\u001a\u00020\u00102\u0006\u0010)\u001a\u00020*2\u0006\u0010-\u001a\u00020.2\u0006\u0010/\u001a\u00020\u0005H\u0002J\u0010\u00100\u001a\u0002012\u0006\u0010-\u001a\u00020.H\u0002R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u001d\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001d\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000eR\u001d\u0010\u0012\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000eR\u001a\u0010\u0014\u001a\u00020\u0015X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001c\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001f\u00a8\u00062"}, d2 = {"Lorg/n9ne/auth/viewModel/CompleteProfileViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "activityLevels", "Ljava/util/ArrayList;", "", "getActivityLevels", "()Ljava/util/ArrayList;", "genders", "getGenders", "ldError", "Landroidx/lifecycle/MutableLiveData;", "Lorg/n9ne/common/Event;", "getLdError", "()Landroidx/lifecycle/MutableLiveData;", "ldShowDate", "", "getLdShowDate", "ldToken", "getLdToken", "navigator", "Lorg/n9ne/common/interfaces/Navigator;", "getNavigator", "()Lorg/n9ne/common/interfaces/Navigator;", "setNavigator", "(Lorg/n9ne/common/interfaces/Navigator;)V", "repo", "Lorg/n9ne/auth/auth/AuthRepo;", "getRepo", "()Lorg/n9ne/auth/auth/AuthRepo;", "setRepo", "(Lorg/n9ne/auth/auth/AuthRepo;)V", "completeProfile", "name", "email", "password", "activity", "gender", "birthdate", "weight", "height", "context", "Landroid/content/Context;", "dateClick", "initDatabase", "data", "Lorg/n9ne/common/source/objects/Auth$CompleteProfile;", "id", "isUserValid", "", "auth_debug"})
public final class CompleteProfileViewModel extends androidx.lifecycle.ViewModel {
    public org.n9ne.common.interfaces.Navigator navigator;
    @org.jetbrains.annotations.Nullable
    private org.n9ne.auth.auth.AuthRepo repo;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<kotlin.Unit>> ldShowDate = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldToken = null;
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<java.lang.String>> ldError = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.ArrayList<java.lang.String> genders = null;
    @org.jetbrains.annotations.NotNull
    private final java.util.ArrayList<java.lang.String> activityLevels = null;
    
    public CompleteProfileViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final org.n9ne.common.interfaces.Navigator getNavigator() {
        return null;
    }
    
    public final void setNavigator(@org.jetbrains.annotations.NotNull
    org.n9ne.common.interfaces.Navigator p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final org.n9ne.auth.auth.AuthRepo getRepo() {
        return null;
    }
    
    public final void setRepo(@org.jetbrains.annotations.Nullable
    org.n9ne.auth.auth.AuthRepo p0) {
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.MutableLiveData<org.n9ne.common.Event<kotlin.Unit>> getLdShowDate() {
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
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<java.lang.String> getGenders() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.util.ArrayList<java.lang.String> getActivityLevels() {
        return null;
    }
    
    public final void completeProfile(@org.jetbrains.annotations.NotNull
    java.lang.String name, @org.jetbrains.annotations.NotNull
    java.lang.String email, @org.jetbrains.annotations.NotNull
    java.lang.String password, @org.jetbrains.annotations.NotNull
    java.lang.String activity, @org.jetbrains.annotations.NotNull
    java.lang.String gender, @org.jetbrains.annotations.NotNull
    java.lang.String birthdate, @org.jetbrains.annotations.NotNull
    java.lang.String weight, @org.jetbrains.annotations.NotNull
    java.lang.String height, @org.jetbrains.annotations.NotNull
    android.content.Context context) {
    }
    
    private final boolean isUserValid(org.n9ne.common.source.objects.Auth.CompleteProfile data) {
        return false;
    }
    
    private final void initDatabase(android.content.Context context, org.n9ne.common.source.objects.Auth.CompleteProfile data, java.lang.String id) {
    }
    
    public final void dateClick() {
    }
}