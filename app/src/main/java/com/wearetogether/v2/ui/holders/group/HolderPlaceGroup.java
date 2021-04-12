package com.wearetogether.v2.ui.holders.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wearetogether.v2.App;
import com.wearetogether.v2.Consts;
import com.wearetogether.v2.R;
import com.wearetogether.v2.app.Book;
import com.wearetogether.v2.app.Like;
import com.wearetogether.v2.app.data.DataUser;
import com.wearetogether.v2.app.model.DataGroup;
import com.wearetogether.v2.app.place.ChangeVisit;
import com.wearetogether.v2.database.model.Place;
import com.wearetogether.v2.ui.activities.MainActivity;
import com.wearetogether.v2.ui.activities.PlaceActivity;
import com.wearetogether.v2.ui.adapters.AdapterGroup;
import com.wearetogether.v2.ui.components.UserViewComponent;
import com.wearetogether.v2.utils.ToastUtils;
import org.jetbrains.annotations.NotNull;

public class HolderPlaceGroup extends HolderItemGroup {
    protected ImageView image_view_like;
    protected TextView text_view_rating;
    protected ImageView image_view_mark;
    protected CheckBox checkbox_user_visit;
    protected CheckBox checkbox_anonymous_visit;
    protected LinearLayout view_content_visiters;
    protected View view_user_visit;
    protected View view_user_vote;
    protected View view_mark;
    protected View view_visit;
    protected View view_anonymous;

    protected View view_visiters;
    protected Long place_unic;
    protected int visit;

    public HolderPlaceGroup(@NonNull @NotNull View itemView, AdapterGroup adapterGroup) {
        super(itemView, adapterGroup);
        view_content_visiters = itemView.findViewById(R.id.view_content_visiters);
        image_view_mark = itemView.findViewById(R.id.image_view_mark);
        image_view_like = itemView.findViewById(R.id.image_view_like);
        text_view_rating = itemView.findViewById(R.id.text_view_rating);
        view_user_visit = itemView.findViewById(R.id.view_user_visit);
        view_user_vote = itemView.findViewById(R.id.view_user_vote);
        view_mark = itemView.findViewById(R.id.view_mark);
        view_visit = itemView.findViewById(R.id.view_visit);
        view_anonymous = itemView.findViewById(R.id.view_anonymous);
        checkbox_user_visit = itemView.findViewById(R.id.checkbox_user_visit);
        checkbox_anonymous_visit = itemView.findViewById(R.id.checkbox_anonymous_visit);
//        checkbox_anonymous_visit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionVisit(listener, -1);
//            }
//        });
        view_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVisit(-1);
            }
        });
//        checkbox_user_visit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                actionVisit(listener, 1);
//            }
//        });
        view_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVisit(1);
            }
        });
        view_user_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLike();
            }
        });
        itemView.findViewById(R.id.view_mark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickToBook(place_unic, v);
            }
        });
    }

    @Override
    public void bind(DataGroup item) {
        this.visit = item.visit;
        text_view_rating.setTag(item.rating);
        text_view_rating.setText(String.valueOf(item.rating));
        if (item.vote == 1) {
            image_view_like.setBackgroundResource(R.drawable.baseline_favorite_black_18dp);
            image_view_like.setTag(1);
        } else {
            image_view_like.setBackgroundResource(R.drawable.baseline_favorite_border_black_18dp);
            image_view_like.setTag(0);
        }
        if (item.save == 1) {
            onSave();
        } else {
            onUnSave();
        }
        if (item.visiters.size() > 0) {
            view_visiters.setVisibility(View.VISIBLE);
            UserViewComponent user_view_component;
            view_content_visiters.removeAllViews();
            for (DataUser dataUser : item.visiters) {
                user_view_component = new UserViewComponent(activity.getApplicationContext());
                user_view_component.setup(activity, dataUser, adapterGroup.cls, adapterGroup.sizeAvatar);
                view_content_visiters.addView(user_view_component);
            }
        } else {
            view_visiters.setVisibility(View.GONE);
        }
        if (item.user_unic == item.uUnic) {
            view_user_visit.setVisibility(View.GONE);
            view_user_vote.setVisibility(View.GONE);
            view_mark.setVisibility(View.GONE);
        } else {
            if (item.anonymous_visit == 1) {
                view_anonymous.setVisibility(View.VISIBLE);
            } else {
                view_anonymous.setVisibility(View.GONE);
            }
            view_user_vote.setVisibility(View.VISIBLE);
            view_mark.setVisibility(View.VISIBLE);
            if (item.visit == 1) {
                checkbox_user_visit.setChecked(true);
                checkbox_anonymous_visit.setChecked(false);
            } else if (item.visit == -1 && item.anonymous_visit == 1) {
                checkbox_anonymous_visit.setChecked(true);
                checkbox_user_visit.setChecked(false);
            } else if (item.visit == 0) {
                checkbox_user_visit.setChecked(false);
                checkbox_anonymous_visit.setChecked(false);
            }
        }


    }

    protected void onClickToBook(Long unic, View view) {
        if (App.SUser == null) return;
        Book.Start(unic, this, view);
    }

    protected void actionLike() {
        if (App.SUser == null) return;
        Like.Start(this, place_unic, Consts.TYPE_PLACE);
    }

    public void onSave() {
        if(activity instanceof PlaceActivity) {
            PlaceActivity placeActivity = (PlaceActivity) activity;
            Place place = placeActivity.getViewModel().mutableLiveData.getValue();
            if(place != null) {
                place.save = 1;
                placeActivity.getViewModel().mutableLiveData.setValue(place);
            }
        }
        image_view_mark.setBackgroundResource(R.drawable.baseline_bookmark_black_18dp);
        image_view_mark.setTag(1);
    }

    public void onUnSave() {
        if(activity instanceof PlaceActivity) {
            PlaceActivity placeActivity = (PlaceActivity) activity;
            Place place = placeActivity.getViewModel().mutableLiveData.getValue();
            if(place != null) {
                place.save = 0;
                placeActivity.getViewModel().mutableLiveData.setValue(place);
            }
        }
        image_view_mark.setBackgroundResource(R.drawable.baseline_bookmark_border_black_18dp);
        image_view_mark.setTag(0);
    }

    public void OnLike() {
        if(activity instanceof PlaceActivity) {
            PlaceActivity placeActivity = (PlaceActivity) activity;
            Place place = placeActivity.getViewModel().mutableLiveData.getValue();
            if(place != null) {
                place.vote = 1;
                placeActivity.getViewModel().mutableLiveData.setValue(place);
            }
        }
        image_view_like.setBackgroundResource(R.drawable.baseline_favorite_black_18dp);
        image_view_like.setTag(1);

        int vote = (int) text_view_rating.getTag();
        text_view_rating.setText(String.valueOf(vote+1));
    }

    public void OnUnLike() {
        if(activity instanceof PlaceActivity) {
            PlaceActivity placeActivity = (PlaceActivity) activity;
            Place place = placeActivity.getViewModel().mutableLiveData.getValue();
            if(place != null) {
                place.vote = 0;
                placeActivity.getViewModel().mutableLiveData.setValue(place);
            }
        }
        image_view_like.setBackgroundResource(R.drawable.baseline_favorite_border_black_18dp);
        image_view_like.setTag(0);

        int vote = (int) text_view_rating.getTag();
        text_view_rating.setText(String.valueOf(vote));
    }

    protected void actionVisit(int visit) {
        if (App.SUser != null) {
            boolean showDialog = false;
            if (this.visit != visit) {
                showDialog = true;
            }
            if (showDialog) {
                showVisitDialog(visit);
            } else {
                setVisit(visit);
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(activity.getString(R.string.please_login_for_visit_place));
            builder.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    public void setVisit(int visit) {
        ChangeVisit.Start(this, place_unic, visit);
    }

    private void showVisitDialog(int visit) {
        if (App.SUser != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(visit == -1 ? activity.getString(R.string.dialog_title_anonymous_visit) : activity.getString(R.string.dialog_title_visit));
            builder.setMessage(visit == -1 ? activity.getString(R.string.dialog_message_anonymous_visit) : activity.getString(R.string.dialog_message_visit));
            builder.setPositiveButton(R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
//                            if (visit == 1) {
//                                if (!Limit) {
//                                    CheckVisit.Start(holder, place_unic, visit, listener);
//                                }
//                            } else {
//                                setVisit(visit, listener);
//                            }
                            setVisit(visit);
                        }
                    }
            );
            builder.setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            boolean is_anonymous = false;
                            boolean is_user = false;
                            if (visit == 1) {
                                OnVisitUser(false);
                            } else {
                                OnVisitAnonymous(false);
                            }
                        }
                    }
            );
            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    public void OnChangeVisit(Integer value) {
        System.out.println("visit " + this.visit);
        System.out.println("visit " + value);
        this.visit = value;
        ToastUtils.Short(activity, "OnChangeVisit " + value);
        if(activity instanceof PlaceActivity) {
            PlaceActivity placeActivity = (PlaceActivity) activity;
            Place place = placeActivity.getViewModel().mutableLiveData.getValue();
            if(place != null) {
                place.visit = visit;
                placeActivity.getViewModel().mutableLiveData.setValue(place);
            }
        }
        if (this.visit == 0) {
            OnVisitAnonymous(false);
            OnVisitUser(false);
        } else {
            if (this.visit == 1) {
                OnVisitAnonymous(false);
                OnVisitUser(true);
            } else {
                OnVisitAnonymous(true);
                OnVisitUser(false);
            }
        }
    }

    public void OnVisitAnonymous(boolean is_anonymous) {
        if (is_anonymous) {
            this.visit = -1;
        } else {
            this.visit = 0;
        }
        checkbox_anonymous_visit.setChecked(is_anonymous);
    }

    public void OnVisitUser(boolean is_user) {
        if (is_user) {
            this.visit = 1;
        } else {
            this.visit = 0;
        }
        checkbox_user_visit.setChecked(is_user);
    }
}
