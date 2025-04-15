package com.ucb.ucbtest.di

import android.content.Context
import com.ucb.data.GithubRepository
import com.ucb.data.LoginRepository
import com.ucb.data.MovieRepository
import com.ucb.data.PushNotificationRepository
import com.ucb.data.datastore.ILoginDataStore
import com.ucb.data.git.IGitRemoteDataSource
import com.ucb.data.git.ILocalDataSource
import com.ucb.data.movie.IMovieRemoteDataSource
import com.ucb.data.push.IPushDataSource
import com.ucb.framework.github.GithubLocalDataSource
import com.ucb.framework.github.GithubRemoteDataSource
import com.ucb.framework.movie.MovieRemoteDataSource
import com.ucb.framework.service.RetrofitBuilder
import com.ucb.ucbtest.R
import com.ucb.usecases.DoLogin
import com.ucb.usecases.FindGitAlias
import com.ucb.usecases.GetPopularMovies
import com.ucb.usecases.SaveGitalias
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.ucb.framework.datastore.LoginDataSource
import com.ucb.framework.push.FirebaseNotificationDataSource
import com.ucb.usecases.GetEmailKey
import com.ucb.usecases.ObtainToken
import com.ucb.framework.expense.ExpenseLocalDataSource
import com.ucb.data.expense.IExpenseDataSource
import com.ucb.usecases.SaveExpense
import com.ucb.usecases.GetAllExpenses
import com.ucb.data.ExpenseRepository
import com.ucb.framework.income.IncomeLocalDataSource
import com.ucb.data.income.IIncomeDataSource
import com.ucb.usecases.SaveIncome
import com.ucb.usecases.GetAllIncomes
import com.ucb.data.IncomeRepository
import com.ucb.usecases.GetAllFinancialRecords
import com.ucb.usecases.DeleteExpense
import com.ucb.usecases.DeleteIncome
import com.ucb.usecases.CheckSufficientBalance
import com.ucb.framework.service.NotificationService
import com.ucb.usecases.CheckBalance
import com.ucb.data.NotificationRepository
import com.ucb.data.notification.INotificationService
import com.ucb.framework.notification.InternalNotificationService
import com.ucb.usecases.NotifyInsufficientFunds

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerRetrofitBuilder(@ApplicationContext context: Context) : RetrofitBuilder {
        return RetrofitBuilder(context)
    }


    @Provides
    @Singleton
    fun gitRemoteDataSource(retrofiService: RetrofitBuilder): IGitRemoteDataSource {
        return GithubRemoteDataSource(retrofiService)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(@ApplicationContext context: Context): ILocalDataSource {
        return GithubLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun gitRepository(remoteDataSource: IGitRemoteDataSource, localDataSource: ILocalDataSource): GithubRepository {
        return GithubRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun provideSaveGitAlias(repository: GithubRepository): SaveGitalias {
        return SaveGitalias(repository)
    }

    @Provides
    @Singleton
    fun provideGitUseCases(githubRepository: GithubRepository): FindGitAlias {
        return FindGitAlias(githubRepository)
    }

    @Provides
    @Singleton
    fun provideGetPopularMovies(movieRepository: MovieRepository, @ApplicationContext context: Context): GetPopularMovies {
        val token = context.getString(R.string.token)
        return GetPopularMovies(movieRepository, token)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(dataSource: IMovieRemoteDataSource) : MovieRepository {
        return MovieRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(retrofit: RetrofitBuilder ): IMovieRemoteDataSource {
        return MovieRemoteDataSource(retrofit)
    }

    @Provides
    @Singleton
    fun provideDoLogin(loginRepository: LoginRepository): DoLogin {
        return DoLogin(loginRepository)
    }

    @Provides
    @Singleton
    fun provideLoginRepository( loginDataSource: ILoginDataStore): LoginRepository {
        return LoginRepository(loginDataSource)
    }

    @Provides
    @Singleton
    fun provideLoginDataSource( @ApplicationContext context: Context ): ILoginDataStore {
        return LoginDataSource(context = context)
    }

    @Provides
    @Singleton
    fun provideGetEmailKey(loginRepository: LoginRepository): GetEmailKey {
        return GetEmailKey(loginRepository)
    }

    @Provides
    @Singleton
    fun provideObtainToken( pushNotificationRepository: PushNotificationRepository): ObtainToken {
        return ObtainToken(pushNotificationRepository)
    }

    @Provides
    @Singleton
    fun providePushNotificationRepository( pushDataSource: IPushDataSource): PushNotificationRepository {
        return PushNotificationRepository(pushDataSource)
    }

    @Provides
    @Singleton
    fun provideIPushDataSource(): IPushDataSource {
        return FirebaseNotificationDataSource()
    }
    @Provides
    @Singleton
    fun provideExpenseLocalDataSource(@ApplicationContext context: Context): IExpenseDataSource {
        return ExpenseLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(dataSource: IExpenseDataSource): ExpenseRepository {
        return ExpenseRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideSaveExpenseUseCase(repository: ExpenseRepository): SaveExpense {
        return SaveExpense(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllExpensesUseCase(repository: ExpenseRepository): GetAllExpenses {
        return GetAllExpenses(repository)
    }
    @Provides
    @Singleton
    fun provideIncomeLocalDataSource(@ApplicationContext context: Context): IIncomeDataSource {
        return IncomeLocalDataSource(context)
    }

    @Provides
    @Singleton
    fun provideIncomeRepository(dataSource: IIncomeDataSource): IncomeRepository {
        return IncomeRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideSaveIncomeUseCase(repository: IncomeRepository): SaveIncome {
        return SaveIncome(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllIncomesUseCase(repository: IncomeRepository): GetAllIncomes {
        return GetAllIncomes(repository)
    }
    @Provides
    @Singleton
    fun provideGetAllFinancialRecordsUseCase(
        expenseRepository: ExpenseRepository,
        incomeRepository: IncomeRepository
    ): GetAllFinancialRecords {
        return GetAllFinancialRecords(expenseRepository, incomeRepository)
    }
    @Provides
    @Singleton
    fun provideDeleteExpenseUseCase(repository: ExpenseRepository): DeleteExpense {
        return DeleteExpense(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteIncomeUseCase(repository: IncomeRepository): DeleteIncome {
        return DeleteIncome(repository)
    }
    @Provides
    @Singleton
    fun provideNotificationService(@ApplicationContext context: Context): NotificationService {
        return NotificationService(context)
    }

    @Provides
    @Singleton
    fun provideCheckSufficientBalance(
        expenseRepository: ExpenseRepository,
        incomeRepository: IncomeRepository
    ): CheckSufficientBalance {
        return CheckSufficientBalance(expenseRepository, incomeRepository)
    }
    @Provides
    @Singleton
    fun provideInternalNotificationService(@ApplicationContext context: Context): INotificationService {
        return InternalNotificationService(context)
    }

    @Provides
    @Singleton
    fun provideNotificationRepository(notificationService: INotificationService): NotificationRepository {
        return NotificationRepository(notificationService)
    }

    @Provides
    @Singleton
    fun provideCheckBalanceUseCase(
        expenseRepository: ExpenseRepository,
        incomeRepository: IncomeRepository
    ): CheckBalance {
        return CheckBalance(expenseRepository, incomeRepository)
    }

    @Provides
    @Singleton
    fun provideNotifyInsufficientFundsUseCase(
        notificationRepository: NotificationRepository
    ): NotifyInsufficientFunds {
        return NotifyInsufficientFunds(notificationRepository)
    }
}