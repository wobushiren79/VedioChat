package mvp.view;

public interface LoadDataView extends BaseView{
   int LOADING_STATUS_REFRESH=1;
   int LOADING_STATUS_MORE=2;
  /**
   * Show a view with a progress bar indicating a loading process.
   * @param msg
   */
  void showLoading(String msg);

  /**
   * Hide a loading view.
   */
  void hideLoading();

  /**
   * Show a retry view in case of an error when retrieving data.
   */
  void showRetry();

  /**
   * Hide a retry view shown if there was an error when retrieving data.
   */
  void hideRetry();

  /**
   * Show an error message
   *
   * @param flag
   * @param message A string representing an error.
   */
  void showError(int flag, String message);

}