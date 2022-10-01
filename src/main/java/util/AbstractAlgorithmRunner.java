package util;

import CGA.Model.Chrome;
import CGA.fileoutput.SolutionListOutput;

import CGA.front.Front;
import CGA.front.impl.ArrayFront;
import CGA.front.util.FrontNormalizer;
import CGA.front.util.FrontUtils;
import CGA.point.PointSolution;
import CGA.qualityIndicator.impl.GenerationalDistance;
import CGA.qualityIndicator.impl.InvertedGenerationalDistance;
import CGA.qualityIndicator.impl.Spread;
import CGA.qualityIndicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;


import java.io.FileNotFoundException;
import java.util.List;

public abstract class AbstractAlgorithmRunner {
    public AbstractAlgorithmRunner() {
    }

    public static void printFinalSolutionSet(List<? extends Chrome> population) {
        (new SolutionListOutput(population)).setSeparator("\t").setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv")).setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv")).print();
       /* JMetalLogger.logger.info("Random seed: " + JMetalRandom.getInstance().getSeed());
        JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
        JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");*/
    }

    public static <S extends Chrome> void printQualityIndicators(List<Chrome> population, String paretoFrontFile) throws FileNotFoundException {
        Front referenceFront = new ArrayFront(paretoFrontFile);
        FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
        Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));
        List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
        String outputString = "\n";
        outputString = outputString + "Hypervolume (N) : " + (new PISAHypervolume(normalizedReferenceFront)).evaluate(normalizedPopulation) + "\n";
        outputString = outputString + "Hypervolume     : " + (new PISAHypervolume(referenceFront)).evaluate(population) + "\n";
        outputString = outputString + "GD (N)          : " + (new GenerationalDistance(normalizedReferenceFront)).evaluate(normalizedPopulation) + "\n";
        outputString = outputString + "GD              : " + (new GenerationalDistance(referenceFront)).evaluate(population) + "\n";
        outputString = outputString + "IGD (N)         : " + (new InvertedGenerationalDistance(normalizedReferenceFront)).evaluate(normalizedPopulation) + "\n";
        outputString = outputString + "IGD             : " + (new InvertedGenerationalDistance(referenceFront)).evaluate(population) + "\n";
        outputString = outputString + "Spread (N)      : " + (new Spread(normalizedReferenceFront)).evaluate(normalizedPopulation) + "\n";
        outputString = outputString + "Spread          : " + (new Spread(referenceFront)).evaluate(population) + "\n";
        JMetalLogger.logger.info(outputString);
    }

    public static <S extends Chrome> double[] printQualityIndicators(List<Chrome> population, String paretoFrontFile ,double[] attrabuteValues) throws FileNotFoundException {
        Front referenceFront = new ArrayFront(paretoFrontFile);
        FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);
        Front normalizedReferenceFront = frontNormalizer.normalize(referenceFront);
        Front normalizedFront = frontNormalizer.normalize(new ArrayFront(population));

        List<PointSolution> normalizedPopulation = FrontUtils.convertFrontToSolutionList(normalizedFront);
        double hv = (new PISAHypervolume(normalizedReferenceFront)).evaluate(normalizedPopulation);
        double gd = (new GenerationalDistance(normalizedReferenceFront)).evaluate(normalizedPopulation);
        double spread =  (new Spread(normalizedReferenceFront)).evaluate(normalizedPopulation);

        attrabuteValues[0]+=hv;
        attrabuteValues[1]+=gd;
        attrabuteValues[2]+=spread;
        return attrabuteValues;
    }
}
