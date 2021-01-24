package es.antonborri.home_widget_example

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.antonborri.home_widget.HomeWidgetProvider



class HomeWidgetExampleProvider : HomeWidgetProvider() {
    private  val PREFERENCES = "HomeWidgetPreferences"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray, widgetData: SharedPreferences) {
        appWidgetIds.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.example_layout).apply {
                //Open App on Widget Click
                setOnClickPendingIntent(R.id.widget_container,
                        PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0))
                val prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                val quotes = prefs.getString("quotes",null);

                setTextViewText(R.id.widget_title,  "Open app")
                setTextViewText(R.id.widget_message,  "Open app to load list")

                if(quotes!=null){
                    val gson = Gson()
                    val itemType = object : TypeToken<List<Quote>>() {}.type
                    val itemList = gson.fromJson<List<Quote>>(quotes, itemType)
                    if(itemList.isNotEmpty()){
                        val element = itemList.randomOrNull();
                        setTextViewText(R.id.widget_title,  element?.quote)
                        setTextViewText(R.id.widget_message, element?.author)
                    }

                }

            }

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
    fun <E> List<E>.randomOrNull(): E? = if (size > 0) random() else null
}

data class Quote(val quote: String, val author: String)