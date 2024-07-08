package com.chalmers.respiradar;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.Locale;

/**
 * Class to create graphs for blood pressure
 */
public class GraphForBP {

    private LineGraphSeries<DataPoint> seriesSBP;
    private LineGraphSeries<DataPoint> seriesDBP;
    private Context context;
    private GraphView graph;
    private int colorSBP;
    private int colorDBP;
    private boolean tapListener;

    /**
     * Constructor for graph
     * Can manually scroll and zoom in x-direction
     * x-bounds are initially 0 to 60
     * @param view the graphView object from the layout
     * @param context application context
     * @param colorSBP color for the graph of SBP
     * @param colorDBP color for the graph of DBP
     * @param screenWidth screen width of the device to set correct padding and textsize
     * @param tapListener if there should be a tap listener
     */
    GraphForBP(View view, final Context context, int colorSBP, int colorDBP, boolean tapListener, int screenWidth) {
        this.context = context;
        this.colorSBP = colorSBP;
        this.colorDBP = colorDBP;
        this.tapListener = tapListener;
        // Graph
        graph = (GraphView) view;
        graph.getGridLabelRenderer().setGridColor(context.getResources().getColor(R.color.colorGraphGrid));
        graph.getGridLabelRenderer().setHorizontalLabelsColor(context.getResources().getColor(R.color.colorGraphGrid));
        graph.getGridLabelRenderer().setVerticalLabelsColor(context.getResources().getColor(R.color.colorGraphGrid));
        graph.getViewport().setBackgroundColor(context.getResources().getColor(R.color.colorGraphBackground));
        graph.getGridLabelRenderer().setTextSize((float)(25*screenWidth/720)); // Scale text size and padding to screen size
        graph.getGridLabelRenderer().setPadding((int)(35*screenWidth/720));
        // Series
        graph.addSeries(newSeriesSBP());        //lägger till serien med mätvärden till grafen.
        graph.addSeries(newSeriesDBP());        //lägger till serien med mätvärden till grafen.
        graph.getViewport().setScrollable(true);        //scrollable in horizontal (x-axis)
        graph.getViewport().setScalable(true);      //för zoomning
        graph.getViewport().setXAxisBoundsManual(true);     //set Viewport window size
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(120);
    }

    /**
     * Getter for the series
     * @return graphs series
     */
    LineGraphSeries<DataPoint> getSeriesSBP() {
        return seriesSBP;
    }

    /**
     * Getter for the series
     * @return graphs series
     */
    LineGraphSeries<DataPoint> getSeriesDBP() {
        return seriesDBP;
    }

    /**
     * Reset the graphs
     * Empties the series
     */
    void resetSeries() {
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(120);
        graph.removeAllSeries();
        graph.addSeries(newSeriesSBP());
        graph.addSeries(newSeriesDBP());
    }

    /**
     * Creates a new series
     * @return new series for SBP
     */
    private LineGraphSeries<DataPoint> newSeriesSBP() {
        seriesSBP = new LineGraphSeries<>();  //skapar en första mätserie
        seriesSBP.setThickness(13);
        seriesSBP.setColor(colorSBP);
        if (tapListener) {
            seriesSBP.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(context, String.format(Locale.getDefault(),"%.0f",
                            dataPoint.getY()) + " bpm at time " + String.format(Locale.getDefault(),"%.0f",
                            dataPoint.getX()) + " s", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return seriesSBP;
    }

    /**
     * Creates a new series
     * @return new series for DBP
     */
    private LineGraphSeries<DataPoint> newSeriesDBP() {
        seriesDBP = new LineGraphSeries<>();  //skapar en första mätserie
        seriesDBP.setThickness(13);
        seriesDBP.setColor(colorDBP);
        if (tapListener) {
            seriesDBP.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(context, String.format(Locale.getDefault(),"%.0f",
                            dataPoint.getY()) + " bpm at time " + String.format(Locale.getDefault(),"%.0f",
                            dataPoint.getX()) + " s", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return seriesDBP;
    }

    Viewport getViewport() {
        return graph.getViewport();
    }

    /**
     * Fixes issues with the graph size
     * Removes the old series and replaces it with a new
     * All old data points is restored in the  new series
     * @param dataPointsSBP array with data points for SBP
     * @param dataPointsDBP array with data points for DBP
     */
    void fixGraphView(Object[] dataPointsSBP, Object[] dataPointsDBP) {
        graph.removeAllSeries();
        graph.addSeries(newSeriesSBP());
        for (Object data : dataPointsSBP) {
            seriesSBP.appendData((DataPoint) data, false, 1000, true);
        }
        graph.addSeries(newSeriesDBP());
        for (Object data : dataPointsDBP) {
            seriesDBP.appendData((DataPoint) data, false, 1000, true);
        }
    }
}
