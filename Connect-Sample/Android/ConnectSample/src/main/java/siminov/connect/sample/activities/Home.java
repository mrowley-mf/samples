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

package siminov.connect.sample.activities;

import siminov.connect.sample.R;
import siminov.connect.sample.StateManager;
import siminov.connect.sample.artist.SliderScroll;
import siminov.connect.sample.artist.SliderScroll.ClickListenerForScrolling;
import siminov.connect.sample.artist.SliderScroll.SizeCallbackForMenu;
import siminov.connect.sample.fragments.TitleBar;
import siminov.connect.sample.model.Book;
import siminov.connect.sample.services.DeleteBook;
import siminov.connect.service.design.IService;
import siminov.core.exception.DatabaseException;
import siminov.core.log.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class Home extends FragmentActivity implements OnClickListener {

	private final int SOURCE_CODE_KEY = 1;
	
	private SliderScroll homeHorizontalScroll = null;
	
	private View home = null;
	private View menuSlider = null;
	
	private LinearLayout sourceCode = null;
	
	public Book selectedBook = null;
	
	public static final String SHOW_MENU_INTENT_HANDLER = Home.class.getName() + "." + "SHOW_MENU_INTENT_HANDLER";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LayoutInflater inflater = LayoutInflater.from(this);
        homeHorizontalScroll = (SliderScroll) inflater.inflate(R.layout.home, null);
        setContentView(homeHorizontalScroll);

        home = inflater.inflate(R.layout.home_artist, null);
        sourceCode = (LinearLayout) home.findViewById(R.id.source_code);
        sourceCode.setOnClickListener(this);

        menuSlider = inflater.inflate(R.layout.menu_slider, null);
        
        final View[] children = new View[] { menuSlider, home };

        // Scroll to app (view[1]) when layout finished.
        int scrollToViewIdx = 1;
        homeHorizontalScroll.initViews(children, scrollToViewIdx, new SizeCallbackForMenu(TitleBar.menuLauncher));
	}
	
	public void onResume() {
		super.onResume();
		registerReceiver(backgroundRefreshHandler, new IntentFilter(SHOW_MENU_INTENT_HANDLER));
		
		StateManager.getInstance().putState(StateManager.ACTIVE_ACTIVITY, this);
	}

	public void onPause() {
		super.onPause();
		
		unregisterReceiver(backgroundRefreshHandler);
	}
	
	public void onDestroy() {
		super.onDestroy();
	}

	public BroadcastReceiver backgroundRefreshHandler = new BroadcastReceiver() {
		
		public void onReceive(Context arg0, Intent arg1) {
			
			String action = arg1.getAction();
			if(action.equalsIgnoreCase(SHOW_MENU_INTENT_HANDLER)) {
				TitleBar.menuLauncher.setOnClickListener(new ClickListenerForScrolling(homeHorizontalScroll, menuSlider));
			}
		}
	};

	public void onClick(View v) {
		
		if(v == sourceCode) {
			onClick(SOURCE_CODE_KEY);
		}
	}
	
	private void onClick(int key) {
		
		switch(key) {
			case SOURCE_CODE_KEY:
				Intent intent = new Intent(this, HomeSourceCode.class);
				startActivity(intent);
				break;
			default:
				break;
		}
	}

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_context_menu, menu);
	}

	public boolean onContextItemSelected(MenuItem item) {

		switch(item.getItemId()) {
	    	case R.id.delete_book:
	    		
		    	IService deleteBookService = new DeleteBook();
		    	deleteBookService.addResource(DeleteBook.BOOK_TITLE, selectedBook.getTitle());
		    		
		    	deleteBookService.invoke();
	
		    	try {
		    		selectedBook.delete().execute();
		    	} catch(DatabaseException de) {
		    		Log.error(Home.class.getName(), "onLongClick", "DatabaseException caught while deleting book from database, " + de.getMessage());
		    	}
	
		    		
		    	siminov.connect.sample.fragments.Home home = (siminov.connect.sample.fragments.Home) StateManager.getInstance().getState(StateManager.ACTIVE_FRAGMENT);
		    	home.refresh();
    			
		    	return true;
	    	default:
	    		return super.onContextItemSelected(item);
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.home_menu, menu);
	    return true;
	}

	
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	        case R.id.add_book:
	        	
	        	Intent intent = new Intent(this, AddBook.class);
	        	startActivity(intent);
	        	
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
