package com.wearetogether.v2.app.download;

import android.content.Context;
import com.wearetogether.v2.App;
import com.wearetogether.v2.database.model.Category;
import com.wearetogether.v2.smodel.SCategory;

import java.util.List;

public class DownloadCategories implements Download {
    private List<SCategory> categories;

    public DownloadCategories(List<SCategory> categories) {
        this.categories = categories;
    }

    @Override
    public void Execute(Context context, String url_base) {
        if(categories.size() > 0) {
            for (SCategory sCategory : categories) {
                Category category = App.Database.daoCategory().getById(Integer.parseInt(sCategory.id));
                if(category == null) {
                    category = new Category();
                    category.id = Integer.parseInt(sCategory.id);
                    category.title = sCategory.title;
                    App.Database.daoCategory().Insert(category);
                } else {
                    category.title = sCategory.title;
                    App.Database.daoCategory().Update(category);
                }
            }
        }
    }
}
