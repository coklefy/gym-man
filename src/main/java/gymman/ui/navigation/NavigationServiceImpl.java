package gymman.ui.navigation;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import gymman.ui.Page;

public class NavigationServiceImpl implements NavigationService {
    private Stack<Page> stack = new Stack<>();
    private Set<Page> pages = new HashSet<>();
    private List<PageChangeHandler> pageChangeHandlers = new ArrayList<>();
    private NavigationParams navParams = new NavigationParams();

    public NavigationServiceImpl() {

    }

    @Override
    public void navigate(String pageId) {
        Optional<Page> page = this.findById(pageId);
        if (!page.isPresent()) {
            throw new NoSuchElementException(String.format("No page found with ID '%s'", pageId));
        }

        this.navigate(page.get());
    }

    @Override
    public void navigate(Page page) {
        try {
            if (this.getCurrentPage().equals(page)) {
                return;
            }
        } catch (EmptyStackException e) {
            // Stack is empty, can go on without issues.
        }

        if (page.canNavigateBackTo()) {
        	this.stack.push(page);
        }

        this.pageChangeHandlers.stream().forEach(h -> h.handle(page));
    }

    @Override
    public void back() {
        if (this.stack.size() < 2) {
            return;
        }

        this.stack.pop();
        this.pageChangeHandlers.stream().forEach(h -> h.handle(this.getCurrentPage()));
    }

    @Override
    public void backOr(String pageId) {
        if (this.stack.size() < 2) {
            this.navigate(pageId);
            return;
        }

        if (this.stack.get(this.stack.size() - 2).getId().equals(pageId)) {
            this.back();
            return;
        }

        this.navigate(pageId);
    }

    @Override
    public void registerPage(Page page) {
        this.pages.add(page);
    }

    @Override
    public Set<Page> getPages() {
        return new HashSet<Page>(this.pages);
    }

    @Override
    public Page getCurrentPage() {
        return this.stack.peek();
    }

    @Override
    public Optional<Page> findById(String id) {
        return this.pages.stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void addOnPageChangeHandler(PageChangeHandler handler) {
        this.pageChangeHandlers.add(handler);
    }

    @Override
    public int getHistorySize() {
        return this.stack.size();
    }

    @Override
    public NavigationParams getNavParams() {
        return this.navParams;
    }
}
