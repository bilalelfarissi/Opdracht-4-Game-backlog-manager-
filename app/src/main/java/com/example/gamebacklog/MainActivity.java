package com.example.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements GameAdapter.GameClickListener {

    /**
     * Binding
     */
    @BindView(R.id.addListButton)
    FloatingActionButton addListButton;
    @BindView(R.id.gameRecyclerView)
    RecyclerView gameRecyclerView;

    /**
     * Create Adapter, List and "Keys"
     */
    private GameAdapter gameAdapter;
    private List<Game> gameList;
    public static final int REQUESTCODE = 1234;
    public static final String EXTRA_GAME = "Game";
    private static AppDatabase db;

    public final static int TASK_GET_ALL_GAMES = 0;
    public final static int TASK_DELETE_GAME = 1;
    public final static int TASK_UPDATE_GAME = 2;
    public final static int TASK_INSERT_GAME = 3;
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_INSERT = "insert";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        db = AppDatabase.getInstance(this);
        new GameAsyncTask(TASK_GET_ALL_GAMES).execute();
        gameRecyclerView.setAdapter(gameAdapter);
        gameRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        deleteGame();
    }

    /**
     * This method is used to indicates that the action that the user made by clicking on a game
     * is an update.
     */
    @Override
    public void GameOnClick(int i) {
        Intent intent = new Intent(this, AddGameActivity.class);
        intent.setAction(ACTION_UPDATE);
        intent.putExtra(EXTRA_GAME, gameList.get(i));
        startActivityForResult(intent, REQUESTCODE);
    }

    /**
     * This method is used to indicate that the user wants to add a new game by clicking on the add
     * list button.
     */
    @OnClick(R.id.addListButton)
    public void navigateToAddGameActivity() {
        Intent intent = new Intent(this, AddGameActivity.class);
        intent.setAction(ACTION_INSERT);
        startActivityForResult(intent, REQUESTCODE);
    }


    /**
     * This method is used to update
     */
    private void updateUI() {
        if (gameAdapter == null) {
            gameAdapter = new GameAdapter(gameList, this);
            gameRecyclerView.setAdapter(gameAdapter);
        } else {
            gameAdapter.swapList(gameList);
            gameAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This method deletes the games when the user is swiping to the left or right
     */
    private void deleteGame() {
        /**
         * Add a touch helper to the RecyclerView to recognize when a user swipes to delete a list entry.
         * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         * and uses callbacks to signal when a user is performing these actions.
         */
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    //Called when a user swipes left or right on a ViewHolder
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        //Get the index corresponding to the selected position
                        int position = (viewHolder.getAdapterPosition());
                        new GameAsyncTask(TASK_DELETE_GAME).execute(gameList.get(position));
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(gameRecyclerView);
    }

    public class GameAsyncTask extends AsyncTask<Game, Void, List> {

        private int taskCode;

        public GameAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(Game... games) {
            switch (taskCode) {
                case TASK_DELETE_GAME:
                    db.gameDao().deleteGames(games[0]);
                    break;
                case TASK_UPDATE_GAME:
                    db.gameDao().updateGames(games[0]);
                    break;
                case TASK_INSERT_GAME:
                    db.gameDao().insertGames(games[0]);
                    break;
            }
            //To return a new list with the updated data, we get all the data from the database again.
            return db.gameDao().getAllGames();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            onGameDbUpdated(list);
        }
    }

    public void onGameDbUpdated(List list) {
        gameList = list;
        updateUI();
    }

    /**
     * This method is used to insert or update the data in the database when the user has edited or
     * insert data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE) {
            if (resultCode == Activity.RESULT_OK) {
                Game game = data.getParcelableExtra(MainActivity.EXTRA_GAME);
                Intent intent = data;
                if (intent.getAction().equals(ACTION_INSERT)) {
                    new GameAsyncTask(TASK_INSERT_GAME).execute(game);
                } else if (intent.getAction().equals(ACTION_UPDATE)) {
                    new GameAsyncTask(TASK_UPDATE_GAME).execute(game);
                }
            }
        }
    }
}
