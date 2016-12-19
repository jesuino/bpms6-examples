package example.executor_update_timer.service;


public interface UpdateTimerService {

	void setAsTriggered(String identifier, long piid);

	void cancelTimer(String identifier, long piid);

	void updateTimerNode(Long piid, String identifier, long delay, long period,
			int repeatLimit);

	// when using java 8 use interface default methods instead ;)
	public static class Factory {
		private static UpdateTimerService updateTimerService;

		static {
			updateTimerService = new UpdateTimerServiceImpl();
		}

		public static UpdateTimerService get() {
			return updateTimerService;
		}
	}
}
