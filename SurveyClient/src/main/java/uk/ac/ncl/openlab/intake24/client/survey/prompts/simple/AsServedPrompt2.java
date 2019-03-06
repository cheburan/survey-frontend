/*
This file is part of Intake24.

© Crown copyright, 2012, 2013, 2014.

This software is licensed under the Open Government Licence 3.0:

http://www.nationalarchives.gov.uk/doc/open-government-licence/
*/

package uk.ac.ncl.openlab.intake24.client.survey.prompts.simple;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.*;
import org.pcollections.PVector;
import org.pcollections.TreePVector;
import org.workcraft.gwt.shared.client.Callback1;
import org.workcraft.gwt.shared.client.Function0;
import org.workcraft.gwt.shared.client.Function1;
import uk.ac.ncl.openlab.intake24.client.api.foods.AsServedImage;
import uk.ac.ncl.openlab.intake24.client.survey.ShepherdTour;
import uk.ac.ncl.openlab.intake24.client.survey.SimplePrompt;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.messages.HelpMessages;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.messages.PromptMessages;
import uk.ac.ncl.openlab.intake24.client.ui.WidgetFactory;


public class AsServedPrompt2 implements SimplePrompt<AsServed2Result> {

    private interface SelectionState {
        <R> R match(Function0<R> less, Function0<R> more, Function1<Integer, R> asShown);
        boolean equals(SelectionState other);
    }

    private static class Less implements SelectionState {
        @Override
        public <R> R match(Function0<R> less, Function0<R> more, Function1<Integer, R> asShown) {
            return less.apply();
        }

        @Override
        public boolean equals(SelectionState other) {
            return other.match(() -> true, () -> false, i -> false);
        }
    }

    private static class More implements SelectionState {
        @Override
        public <R> R match(Function0<R> less, Function0<R> more, Function1<Integer, R> asShown) {
            return more.apply();
        }

        @Override
        public boolean equals(SelectionState other) {
            return other.match(() -> false, () -> true, i -> false);
        }
    }

    private static class AsShown implements SelectionState {
        public final int selectedIndex;

        public AsShown(int selectedIndex) {
            this.selectedIndex = selectedIndex;
        }

        @Override
        public <R> R match(Function0<R> less, Function0<R> more, Function1<Integer, R> asShown) {
            return asShown.apply(selectedIndex);
        }

        @Override
        public boolean equals(SelectionState other) {
            return other.match(() -> false, () -> false, i -> i == selectedIndex);
        }
    }

    private static final Less LESS = new Less();
    private static final More MORE = new More();

    private static final String STYLE_THUMBNAIL_SELECTED = "intake24-as-served-thumbnail-selected";

    private static final HelpMessages helpMessages = HelpMessages.INSTANCE;

    private final AsServedImage[] images;
    private final SafeHtml promptText;
    private final String prevButtonLabel;
    private final String nextButtonLabel;
    private final String confirmButtonLabel;
    private final boolean moreThanOptionEnabled;

    private FlowPanel[] imageDivs;
    private Image[] imageThumbnails;
    private FlowPanel lessThumbnail;
    private FlowPanel moreThumbnail;
    private Button nextButton;
    private Button prevButton;
    private Button confirmButton;
    private FlowPanel imageContainer;
    private FlowPanel thumbsContainer;


    private SelectionState selectionState;
    private int mainImageIndex;
    private boolean mainImageSwitching = false;

    public AsServedPrompt2(AsServedImage[] images, SafeHtml promptText, String prevButtonLabel, String nextButtonLabel,
                           String confirmButtonLabel, boolean moreThanOptionEnabled) {
        this.images = images;
        this.promptText = promptText;
        this.prevButtonLabel = prevButtonLabel;
        this.nextButtonLabel = nextButtonLabel;
        this.confirmButtonLabel = confirmButtonLabel;
        this.moreThanOptionEnabled = moreThanOptionEnabled;
    }

    private final static PVector<ShepherdTour.Step> tour = TreePVector
            .<ShepherdTour.Step>empty()
            .plus(new ShepherdTour.Step("image", "#intake24-as-served-image-container", helpMessages.asServed_imageTitle(), helpMessages
                    .asServed_imageDescription()))
            .plus(new ShepherdTour.Step("label", "#intake24-as-served-image-container", helpMessages.asServed_labelTitle(), helpMessages
                    .asServed_labelDescription(), "top right", "bottom right"))
            .plus(new ShepherdTour.Step("thumbs", "#intake24-as-served-thumbs-container", helpMessages.asServed_thumbsTitle(), helpMessages
                    .asServed_thumbsDescription()))
            .plus(new ShepherdTour.Step("hadLess", "#intake24-as-served-prev-button", helpMessages.asServed_prevButtonTitle(), helpMessages
                    .asServed_prevButtonDescription(), "bottom left", "top left"))
            .plus(new ShepherdTour.Step("hadMore", "#intake24-as-served-next-button", helpMessages.asServed_nextButtonTitle(), helpMessages
                    .asServed_nextButtonDescription(), "bottom left", "top left"))
            .plus(new ShepherdTour.Step("hadThisMuch", "#intake24-as-served-confirm-button", helpMessages.asServed_confirmButtonTitle(), helpMessages
                    .asServed_confirmButtonDescription(), "bottom right", "top right"));


    private void switchMainImage(int newIndex) {

        if (mainImageIndex == newIndex)
            return;

        imageDivs[newIndex].getElement().getStyle().clearDisplay();
        imageDivs[newIndex].getElement().getStyle().setOpacity(0);
        imageDivs[newIndex].getElement().getStyle().setZIndex(400);
        imageDivs[newIndex].addStyleName("intake24-as-served-image-overlay");

        imageDivs[mainImageIndex].getElement().getStyle().setZIndex(399);

        mainImageSwitching = true;

        Animation fadeIn = new Animation() {
            @Override
            protected void onUpdate(double progress) {
                imageDivs[newIndex].getElement().getStyle().setOpacity(progress);
            }

            @Override
            protected void onComplete() {
                imageDivs[newIndex].getElement().getStyle().setOpacity(1);
                imageDivs[newIndex].removeStyleName("intake24-as-served-image-overlay");

                imageDivs[mainImageIndex].getElement().getStyle().setDisplay(Display.NONE);
                imageDivs[mainImageIndex].getElement().getStyle().setZIndex(0);

                mainImageIndex = newIndex;
                mainImageSwitching = false;
            }
        };

        fadeIn.run(400);
    }

    private void setSelectionState(final SelectionState newState) {
        if (mainImageSwitching || selectionState.equals(newState))
            return;

        newState.match(
                () -> {
                    prevButton.setEnabled(false);
                    lessThumbnail.addStyleName(STYLE_THUMBNAIL_SELECTED);

                    selectionState.match(
                            () -> (),
                            () -> {
                                moreThumbnail.removeStyleName(STYLE_THUMBNAIL_SELECTED);
                            },
                            i -> {
                                imageThumbnails[i].removeStyleName(STYLE_THUMBNAIL_SELECTED);
                            }

                    );
                },
                () -> {},
                i -> {}
        )

        if (selectionState.equals(LESS))
            prevButton.setEnabled(false);
        else
            prevButton.setEnabled(true);

        if (selectionState.equals(MORE))
            nextButton.setEnabled(false);
        else
            nextButton.setEnabled(true);

        imageThumbnails[newIndex].addStyleName("intake24-as-served-thumbnail-selected");
        imageThumbnails[selectedIndex].removeStyleName("intake24-as-served-thumbnail-selected");
    }

    private void prev() {
        int newIndex = selectedIndex;
        if (newIndex > 0) {
            newIndex--;
            setSelectionState(newIndex);
        }
    }

    private void next() {
        int newIndex = selectedIndex;
        if (newIndex < imageDivs.length - 1) {
            newIndex++;
            setSelectionState(newIndex);
        }
    }

    @Override
    public FlowPanel getInterface(final Callback1<AsServed2Result> onComplete) {
        final FlowPanel content = new FlowPanel();

//        PromptMessages.INSTANCE.asServed_servedLessButtonLabel(), PromptMessages.INSTANCE.asServed_servedMoreButtonLabel(),
  //              PromptMessages.INSTANCE.asServed_servedContinueButtonLabel(),

        FlowPanel promptPanel = WidgetFactory.createPromptPanel(promptText, ShepherdTour.createTourButton(tour, AsServedPrompt2.class.getSimpleName()));
        content.add(promptPanel);

        FlowPanel asServedContainer = new FlowPanel();
        asServedContainer.addStyleName("intake24-as-served-image-chooser");

        imageContainer = new FlowPanel();
        imageContainer.addStyleName("intake24-as-served-image-container");
        imageContainer.getElement().setId("intake24-as-served-image-container");

        thumbsContainer = new FlowPanel();
        thumbsContainer.addStyleName("intake24-as-served-thumbs-container");
        thumbsContainer.getElement().setId("intake24-as-served-thumbs-container");

        prevButton = WidgetFactory.createButton(prevButtonLabel, "intake24-as-served-prev-button", e -> prev());
        nextButton = WidgetFactory.createButton(nextButtonLabel, "intake24-as-served-next-button", e -> next());
        confirmButton = WidgetFactory.createGreenButton(confirmButtonLabel, "intake24-as-served-confirm-button", e -> {
            //onComplete.call(new AsServed2Result(selectedIndex, def.));
        });

        asServedContainer.add(imageContainer);
        asServedContainer.add(thumbsContainer);
        asServedContainer.add(WidgetFactory.createButtonsPanel(prevButton, nextButton, confirmButton));

        imageDivs = new FlowPanel[images.length];
        imageThumbnails = new Image[images.length];
        final NumberFormat nf = NumberFormat.getDecimalFormat();


        lessThumbnail = new FlowPanel();
        lessThumbnail.addStyleName("intake24-as-served-thumbnail");
        lessThumbnail.addStyleName("intake24-as-served-more");

        Image lessThumbnailImage = new Image(images[0].thumbnailUrl);

        HTMLPanel lessThumbnailIcon = new HTMLPanel("p", "-");

        lessThumbnail.add(lessThumbnailImage);
        lessThumbnail.add(lessThumbnailIcon);

        thumbsContainer.add(lessThumbnail);

        for (int i = 0; i < images.length; i++) {
            imageDivs[i] = new FlowPanel();
            imageDivs[i].getElement().getStyle().setDisplay(Display.NONE);

            Image image = new Image(images[i].mainImageUrl);
            image.addStyleName("intake24-as-served-image");
            imageDivs[i].add(image);

            Label label = new Label(nf.format(Math.round(images[i].weight)) + " " + PromptMessages.INSTANCE.asServed_weightUnitLabel());

            label.addStyleName("intake24-as-served-image-label");
            label.getElement().setId("intake24-as-served-image-label");

            imageDivs[i].add(label);

            imageContainer.add(imageDivs[i]);

            imageThumbnails[i] = new Image(images[i].thumbnailUrl);

            final int k = i;

            imageThumbnails[i].addClickHandler(e -> setSelectionState(k));

            imageThumbnails[i].addStyleName("intake24-as-served-thumbnail");
            thumbsContainer.add(imageThumbnails[i]);
        }


        moreThumbnail = new FlowPanel();
        moreThumbnail.addStyleName("intake24-as-served-thumbnail");
        moreThumbnail.addStyleName("intake24-as-served-more");

        Image moreThumbnailImage = new Image(images[images.length - 1].thumbnailUrl);

        HTMLPanel moreThumbnailIcon = new HTMLPanel("p", "+");

        moreThumbnail.add(moreThumbnailImage);
        moreThumbnail.add(moreThumbnailIcon);

        thumbsContainer.add(moreThumbnail);

        int startingIndex = images.length / 2;

        imageDivs[startingIndex].getElement().getStyle().clearDisplay();
        imageThumbnails[startingIndex].addStyleName("intake24-as-served-thumbnail-selected");

        selectionState = new AsShown(startingIndex);

        content.add(asServedContainer);

        ShepherdTour.makeShepherdTarget(promptPanel, imageContainer, thumbsContainer, nextButton, prevButton, confirmButton);

        return content;
    }

    @Override
    public String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "As served portion size prompt";
    }
}