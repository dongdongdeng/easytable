package org.vandeseer.easytable.structure.cell;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
@SuperBuilder(toBuilder = true)
public class CellCustom extends CellBaseData {

    @AllArgsConstructor
    public static class DrawingContext {
        public final PDPageContentStream contentStream;
        public final float x;
        public final float y;
    }

    @Builder.Default
    private Supplier<Float> heightSupplier = () -> 0f;

    @Builder.Default
    private Consumer<DrawingContext> onDrawConsumer = context -> {
    };

    @Override
    public float getHeight() {
        return heightSupplier.get();
    }

    public void onDraw(PDPageContentStream contentStream, final float lowerLeftX, final float lowerLeftY) {
        try {
            onDrawConsumer.accept(new DrawingContext(contentStream, lowerLeftX, lowerLeftY));
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

}
