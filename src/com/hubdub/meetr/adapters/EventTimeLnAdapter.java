package com.hubdub.meetr.adapters;

import java.util.ArrayList;

import org.json.JSONException;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hubdub.meetr.R;
import com.hubdub.meetr.models.EventActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseFile;
import com.parse.ParseObject;

public class EventTimeLnAdapter extends ArrayAdapter<EventActivity> {
        Context mContext;
        private ImageLoadHelper imageLoadHelper;
        private ImageLoader imageLoader;

        public EventTimeLnAdapter(Context context, ArrayList<EventActivity> arrayList) {
                super(context, 0, arrayList);
                mContext = context;

                // Using a helper class. Keeps the adapter clean and tidy.
                imageLoadHelper = new ImageLoadHelper(context);
                
                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        		imageLoader = ImageLoader.getInstance();
        		imageLoader.init(config);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
                if (view == null) {
                        view = LayoutInflater.from(mContext).inflate(com.hubdub.meetr.R.layout.timeline_activity_post, parent,false);
                }
                
                final EventActivity activity = getItem(position);
                TextView tvPost = (TextView) view.findViewById(R.id.eventPost);
                TextView tvPostBy = (TextView) view.findViewById(R.id.postBy);
                final ImageView imageView = (ImageView) view.findViewById(R.id.ivPhotoPost);
                ImageView ivProfilePhoto = (ImageView) view.findViewById(R.id.ivFriends);
                
                if(activity.getObjectId() != null){
                        
                        if(activity.getString("activityType").equals("post")) {
                                imageView.setVisibility(View.GONE);
                                tvPost.setVisibility(View.VISIBLE);
                                
                                try {
                                        ParseObject obj = activity.getParseObject("postPtr");
                                        ParseObject eventObj = activity.getParseObject("activityFrom");
                                        
                                        String fbId = eventObj.getJSONObject("profile").getString("facebookId");
                                        
                                        String imageUrl = "http://graph.facebook.com/" + fbId + "/picture?type=square";
                                        imageLoader.displayImage(imageUrl, ivProfilePhoto);
                                        
                                        
                                        
                                        tvPost.setText(obj.getString("post"));
                                        System.out.println("getting post: " + obj.getString("post").hashCode());
                                        tvPostBy.setText(eventObj.getJSONObject("profile").getString("name"));
                                                
                                        
                                        
                                } catch (JSONException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "1: " + e.toString());
                                }        catch (IllegalStateException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "2: " + e.toString());
                                } catch (NullPointerException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "3: " + e.toString());
                                }
                        } else if (activity.getString("activityType").equals("photo")) {
                                imageView.setVisibility(View.VISIBLE);
                                tvPost.setVisibility(View.GONE);
                        
                                try {
                                        ParseObject obj = activity.getParseObject("photoPtr");
                                        ParseObject eventObj = activity.getParseObject("activityFrom");
                                        
                                        String fbId = eventObj.getJSONObject("profile").getString("facebookId");
                                        
                                        String imageUrl = "http://graph.facebook.com/" + fbId + "/picture?type=square";
                                        imageLoader.displayImage(imageUrl, ivProfilePhoto);
                                        
                                        ParseFile photoFile = obj.getParseFile("photo");
                                        imageLoadHelper.loadBitmap(photoFile, imageView);
                                        
                                        System.out.println("getting photo file: " + photoFile.hashCode());
                                        tvPostBy.setText(eventObj.getJSONObject("profile").getString("name"));
        
                                } catch (JSONException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "4: " + e.toString());
                                } catch (IllegalStateException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "5: " + e.toString());
                                } catch (NullPointerException e) {
                                        Toast.makeText(getContext(), "We weren't able to do that. Sorry!", Toast.LENGTH_SHORT).show();
                                        Log.d("ERROR", "6: " + e.toString());
                                }
                        }
                } else {
                        tvPost.setText("Loading...");
                        tvPostBy.setText("");
                }

                return view;
        }
        

}
