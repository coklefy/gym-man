package gymman.ui.navigation;

import java.util.HashMap;
import java.util.Map;

public class NavigationParams {
    Map<String, Object> items = new HashMap<>();

    public NavigationParams() {

    }

    public void set(String key, Object value) {
        this.items.put(key, value);
    }

    public Object get(String key) {
        return this.items.remove(key);
    }

    public Object getWithoutRemoving(String key) {
        return this.items.get(key);
    }

    public void remove(String key) {
        this.items.remove(key);
    }
}
