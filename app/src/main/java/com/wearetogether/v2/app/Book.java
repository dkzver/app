package com.wearetogether.v2.app;

import android.view.View;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.R;
import com.wearetogether.v2.ui.holders.group.HolderPlaceGroup;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class Book {
    public static void Start(Long unic, final HolderPlaceGroup holder, View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                com.wearetogether.v2.database.model.Book log_book = App.Database.daoBook().get(unic);
                if (log_book == null) {
                    log_book = new com.wearetogether.v2.database.model.Book();
                    log_book.unic = Calendar.getInstance().getTimeInMillis();
                    log_book.item_unic = unic;
                    App.Database.daoBook().insert(log_book);
                    holder.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.onSave();
                            Snackbar snackbar = Snackbar.make(
                                    view,
                                    holder.activity.getString(R.string.book_save),
                                    Snackbar.LENGTH_LONG
                            );
                            snackbar.show();
                        }
                    });
                } else {
                    App.Database.daoBook().delete(log_book);
                    holder.activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            holder.onUnSave();
                            Snackbar snackbar = Snackbar.make(
                                    view,
                                    holder.activity.getString(R.string.book_un_save),
                                    Snackbar.LENGTH_LONG
                            );
                            snackbar.show();
                        }
                    });
                }

            }
        }).start();
    }
}
