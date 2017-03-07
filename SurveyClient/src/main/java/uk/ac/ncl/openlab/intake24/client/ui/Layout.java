package uk.ac.ncl.openlab.intake24.client.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import uk.ac.ncl.openlab.intake24.client.CommonMessages;

import java.util.logging.Logger;

public class Layout {
    private static final Logger logger = Logger.getLogger(Layout.class.getName());

    private static final CommonMessages messages = CommonMessages.Util.getInstance();

    public static final RootPanel UIRootPanel = RootPanel.get("UIContainer");

    private static FlowPanel headerContainer;
    private static FlowPanel header;

    private static FlowPanel navbarContainer;
    private static FlowPanel navbar;

    private static FlowPanel mainContentContainer;
    private static FlowPanel mainContent;

    private static FlowPanel footerContainer;
    private static FlowPanel footer;

    // public static final

    public static final Anchor watchTutorial = WidgetFactory.createTutorialVideoLink();
    public static final Anchor logOut = new Anchor(SafeHtmlUtils.fromSafeConstant(messages.callbackRequestForm_success()));


    public static void setNavBarLinks(Anchor... links) {
        navbar.clear();

        HTMLPanel ul = new HTMLPanel("ul", "");

        for (Anchor a : links) {
            HTMLPanel li = new HTMLPanel("li", "");
            li.add(a);
            ul.add(li);
        }

        navbar.add(ul);
    }

    public static void setMainContent(Widget content) {
        mainContent.add(content);
    }

    private static void onResize() {
        logger.fine("resize");


        int windowHeight = Window.getClientHeight();

        int headerHeight = headerContainer.getOffsetHeight();
        int navbarHeight = navbarContainer.getOffsetHeight();
        int footerHeight = footerContainer.getOffsetHeight();

        int minHeight = windowHeight - headerHeight - navbarHeight - footerHeight;

        mainContent.getElement().getStyle().setProperty("minHeight", minHeight, Style.Unit.PX);


                /*
                var minHeight = windowHeight - headerHeight - navbarHeight - footerHeight;
                var panelHeight = $('.intake24-meals-panel .intake24-meal-list').height() + 100;

                if (minHeight < panelHeight) {
                    minHeight = panelHeight;
                }
                ;

                // Content height
                $(".intake24-main-content").css("min-height", minHeight);

                // Side panel
                $('.intake24-meals-panel').css('height', minHeight);

                // Check panel position
                if ($('body').width() >= '960') {
                    $("#intake24-meals-panel").css('margin-left', '0px');
                    $(".intake24-meals-panel-header-button").removeClass('button-show');
                    $(".intake24-meals-panel-header-button").removeClass('button-hide');
                    $(".intake24-meals-panel-header-button").addClass('button-menu');
                } else {
                    if (!panelShowing)
                        $("#intake24-meals-panel").css('margin-left', '-230px');
                    $(".intake24-meals-panel-header-button").removeClass('button-menu');
                    $(".intake24-meals-panel-header-button").removeClass('button-hide');
                    $(".intake24-meals-panel-header-button").addClass('button-show');
                }

                bindEvents();*/
    }

    public static void createMainPageLayout() {
        headerContainer = new FlowPanel();
        headerContainer.addStyleName("intake24-header-container");

        header = new FlowPanel();
        header.addStyleName("intake24-header");
        header.add(new HTMLPanel("h1", "Intake24"));
        headerContainer.add(header);

        navbarContainer = new FlowPanel();
        navbarContainer.addStyleName("intake24-navbar-container");

        navbar = new FlowPanel();
        navbar.addStyleName("intake24-navbar");
        navbarContainer.add(navbar);

        mainContentContainer = new FlowPanel();
        mainContentContainer.addStyleName("intake24-main-content-container");

        mainContent = new FlowPanel();
        mainContent.addStyleName("intake24-main-content");
        mainContentContainer.add(mainContent);

        footerContainer = new FlowPanel();
        footerContainer.addStyleName("intake24-footer-container");

        footer = new FlowPanel();
        footer.addStyleName("intake24-footer");
        footerContainer.add(footer);

        Anchor nuLogo = new Anchor();
        nuLogo.addStyleName("intake24-footer-nu-logo");
        nuLogo.setHref("https://openlab.ncl.ac.uk/");

        Anchor fssLogo = new Anchor();
        fssLogo.addStyleName("intake24-footer-fss-logo");
        fssLogo.setHref("http://www.foodstandards.gov.scot/");

        footer.add(nuLogo);
        footer.add(fssLogo);

        UIRootPanel.clear();
        UIRootPanel.add(headerContainer);
        UIRootPanel.add(navbarContainer);
        UIRootPanel.add(mainContentContainer);
        UIRootPanel.add(footerContainer);

        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent resizeEvent) {
                Layout.onResize();
            }
        });

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                onResize();
            }
        });

        /* <div id="main-content-container">
        <div id="main-content" style="min-height: 697px;"><div class="intake24-survey-content-container"><div><iframe width="560" height="315" src="https://www.youtube.com/embed/5vB4NgI4ATc" frameborder="0" allowfullscreen=""></iframe></div><div><p style="font-size: 120%;">Welcome to Intake24!</p><p>We would like you to tell us everything you had to eat and drink yesterday. Please include all meals, snacks and drinks (including water and alcohol).</p><p>It may help you to think about what you did yesterday:</p><ul style="list-style-type: disc;"><li>What time did you wake up?</li><li>Were you at school, college, home, work?</li><li>Who were you with?</li><li>What time did you go to sleep?</li></ul></div><button type="button" class="gwt-Button intake24-button intake24-green-button">I am ready!</button></div></div>
	</div>*/
    }
}
