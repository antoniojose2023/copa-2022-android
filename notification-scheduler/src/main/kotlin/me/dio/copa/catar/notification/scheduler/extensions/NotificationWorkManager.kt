package me.dio.copa.catar.notification.scheduler.extensions

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.delay
import me.dio.copa.catar.domain.model.MatchDomain
import java.time.Duration
import java.time.LocalDateTime

private const val NOTIFICATION_TITLE = "NOTIFICATION_TITLE_KEY"
private const val NOTIFICATION_CONTENT = "NOTIFICATION_CONTENT_KEY"

class NotificationWorkManager(val context: Context, val params: WorkerParameters): Worker(context, params) {

    override fun doWork(): Result {

        val title = (inputData.getString(NOTIFICATION_TITLE) ?: java.lang.IllegalArgumentException("Erro conteúdo inexistente")) as String
        val content = (inputData.getString(NOTIFICATION_CONTENT) ?: java.lang.IllegalArgumentException("Erro conteúdo inexistente")) as String

        context.showNotification(title, content)

        return Result.success()

    }

    companion object{
        fun start(context: Context, matchDomain: MatchDomain){

            val (id, _, _,_,_, date) = matchDomain

            val delayInitial: Duration = Duration.between(date, LocalDateTime.now().minusMinutes(5))

            val inputData = workDataOf(
                NOTIFICATION_TITLE to "O jogo já vai começar. . .",
                NOTIFICATION_CONTENT to "Jodo de hoje - ${matchDomain.team1.flag} x ${matchDomain.team2.flag}"
            )

            WorkManager.getInstance(context).enqueueUniqueWork(
                 id,
                 ExistingWorkPolicy.KEEP,
                 createResquest(delayInitial, inputData)
            )

        }


        fun cancel(context: Context, matchDomain: MatchDomain){
            WorkManager.getInstance(context)
                .cancelUniqueWork(matchDomain.id)
        }

        private fun createResquest(delay: Duration, inputData: Data): OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<NotificationWorkManager>()
                .setInitialDelay(delay)
                .setInputData(inputData)
                .build()
    }


}