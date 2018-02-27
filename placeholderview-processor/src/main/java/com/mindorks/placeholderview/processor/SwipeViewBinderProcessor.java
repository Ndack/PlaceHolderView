package com.mindorks.placeholderview.processor;

import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

public class SwipeViewBinderProcessor extends ViewBinderProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Layout.class)) {
            try {
                SwipeViewBinderClassStructure
                        .create(Validator.validateLayout((TypeElement) Validator.validateTypeElement(element)),
                                getElementUtils())
                        .addConstructor()
                        .addResolveViewMethod()
                        .addRecycleViewMethod()
                        .addUnbindMethod()
                        .addBindViewPositionMethod()
                        .addBindViewMethod()
                        .addBindClickMethod()
                        .addBindLongClickMethod()
                        .addBindSwipeViewMethod()
                        .prepare()
                        .generate(getFiler());
            } catch (IOException e) {
                getMessager().printMessage(Diagnostic.Kind.ERROR, e.toString(), element);
                return true;
            }
        }
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new TreeSet<>(Arrays.asList(
                SwipeView.class.getCanonicalName(),
                SwipeIn.class.getCanonicalName(),
                SwipeOut.class.getCanonicalName(),
                SwipeInState.class.getCanonicalName(),
                SwipeOutState.class.getCanonicalName(),
                SwipeCancelState.class.getCanonicalName(),
                SwipeHead.class.getCanonicalName()));
        annotations.addAll(super.getSupportedAnnotationTypes());
        return annotations;
    }
}

