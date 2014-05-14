package Utils.SQL;

public enum SQLcmd {
    TVShows_genres, TVShows_search, TVShows_order_all, TVShows_order_followed, TVShows_order_recommended, TVShows_search_by_genre,
    Validation_password,
    ShowTemplate_show_info, ShowTemplate_show_seasons, ShowTemplate_show_follow, ShowTemplate_show_unfollow, ShowTemplate_get_comments,
    ShowTemplate_show_check_follows, ShowTemplate_show_rate_update, ShowTemplate_show_rate_insert, ShowTemplate_show_rate_select,
    Show_set_season_seen, Show_set_season_unseen, Show_set_show_seen, Show_set_show_unseen, Show_get_season_id, Show_exists,
    Account_user_data, Account_followed_shows, Account_check_login, Account_create_user, Account_update_user, Account_delete_user,
    Show_get_season, Show_get_episodes, Show_set_episode_seen, Show_set_episode_unseen, Show_get_episode_seen,
    Admin_insert_show, Admin_insert_season, Admin_insert_episode, Admin_insert_genre, Admin_select_genre, Admin_associate_genre;
}
