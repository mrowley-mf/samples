/**
 * [SIMINOV FRAMEWORK]
 * Copyright [2014-2016] [Siminov Software Solution LLP|support@siminov.com]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/


package siminov.core.sample.fragments;

import siminov.core.sample.R;
import siminov.core.sample.model.Book;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleBar extends Fragment {

	public static ImageView menuLauncher = null;
	private TextView title = null;
	private View titleBar = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.title_bar, container);
	}

	public void onResume() {
		super.onResume();
		
		titleBar = getView();
		
		title = (TextView) titleBar.findViewById(R.id.title);
		menuLauncher = (ImageView) titleBar.findViewById(R.id.menu_launcher);
		
		if(getActivity() instanceof siminov.core.sample.activities.Home) {
			getActivity().sendBroadcast(new Intent(siminov.core.sample.activities.Home.SHOW_MENU_INTENT_HANDLER));
		}
		
		initialize();
	}
	
	private void initialize() {
		Activity currentActivity = getActivity();
		if(currentActivity instanceof siminov.core.sample.activities.Home) {
			title.setText(getResources().getString(R.string.books));
		} else if(currentActivity instanceof siminov.core.sample.activities.BookDetail) {
			Book book = ((siminov.core.sample.activities.BookDetail) currentActivity).getBook();
			title.setText(book.getTitle());
			
			menuLauncher.setVisibility(View.INVISIBLE);
		} else if(currentActivity instanceof siminov.core.sample.activities.Login) {
			title.setText(R.string.welcome_note);
			
			menuLauncher.setVisibility(View.INVISIBLE);
		}
	}
}
