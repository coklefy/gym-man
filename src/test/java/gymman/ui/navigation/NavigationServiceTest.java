package gymman.ui.navigation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.EmptyStackException;
import org.junit.Before;
import org.junit.Test;
import gymman.ui.Controller;
import gymman.ui.Page;

public class NavigationServiceTest {
    NavigationService nav;

    Page page1;
    Page page2;
    Controller ctrl;

    @Before
    public void setUp() throws Exception {
        this.nav = new NavigationServiceImpl();

        this.ctrl = mock(Controller.class);

        this.page1 = mock(Page.class);
        when(this.page1.getId()).thenReturn("page1");
        when(this.page1.getTitle()).thenReturn("Page 1");
        when(this.page1.getController()).thenReturn(this.ctrl);
        when(this.page1.canNavigateBackTo()).thenReturn(true);

        this.page2 = mock(Page.class);
        when(this.page2.getId()).thenReturn("page2");
        when(this.page2.getTitle()).thenReturn("Page 2");
        when(this.page2.canNavigateBackTo()).thenReturn(true);
    }

    @Test
    public void testCanRegisterPage() {
        this.nav.registerPage(this.page1);
        assertThat(this.nav.getPages().size(), is(1));
    }

    @Test
    public void testCanNavigateToPageByPageId() {
        this.nav.registerPage(this.page1);
        this.nav.navigate("page1");

        assertThat(this.nav.getCurrentPage().getId(), is("page1"));
    }

    @Test
    public void testCanNavigateToPageByPageObject() {
        this.nav.registerPage(this.page1);
        this.nav.navigate(this.page1);

        assertThat(this.nav.getCurrentPage().getId(), is("page1"));
    }

    @Test
    public void testCanGoBackToPreviousPage() {
        this.nav.registerPage(this.page1);
        this.nav.registerPage(this.page2);

        this.nav.navigate("page1");
        this.nav.navigate("page2");

        assertThat(this.nav.getCurrentPage().getId(), is("page2"));

        this.nav.back();

        assertThat(this.nav.getCurrentPage().getId(), is("page1"));
    }

    @Test(expected = EmptyStackException.class)
    public void testErrorWhenGettingCurrentPageButNoNavigationOccurredYet() {
        this.nav.getCurrentPage();
    }

    @Test
    public void testPageChangeHandlerCalledOnlyOnceWhenNavigatingToTheSamePage() {
        this.nav.registerPage(this.page1);

        PageChangeHandler handler = mock(PageChangeHandler.class);

        this.nav.addOnPageChangeHandler(handler);


        this.nav.navigate("page1");
        this.nav.navigate("page1");

        verify(handler, times(1)).handle(this.page1);
    }

    @Test
    public void testBackOrGoesBackWhenCorrectPreviousPage() {
        this.nav.registerPage(this.page1);
        this.nav.registerPage(this.page2);

        this.nav.navigate("page1");
        this.nav.navigate("page2");

        this.nav.backOr("page1");
        assertThat(this.nav.getCurrentPage().getId(), is("page1"));
        assertThat(this.nav.getHistorySize(), is(1));
    }

    @Test
    public void testBackOrNavigatesWhenIncorrectPreviousPage() {
        Page page3 = mock(Page.class);
        when(page3.getId()).thenReturn("page3");

        this.nav.registerPage(this.page1);
        this.nav.registerPage(this.page2);
        this.nav.registerPage(page3);

        this.nav.navigate("page3");
        this.nav.navigate("page2");

        this.nav.backOr("page1");
        assertThat(this.nav.getCurrentPage().getId(), is("page1"));
    }

    @Test
    public void testBackOrNavigatesWhenSinglePageInStack() {
        this.nav.registerPage(this.page1);
        this.nav.registerPage(this.page2);

        this.nav.navigate("page1");
        
        this.nav.backOr("page2");
        assertThat(this.nav.getCurrentPage().getId(), is("page2"));
    }
}
