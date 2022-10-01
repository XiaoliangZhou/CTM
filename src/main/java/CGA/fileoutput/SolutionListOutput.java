package CGA.fileoutput;


import CGA.Model.Chrome;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.fileoutput.FileOutputContext;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class SolutionListOutput {
    private FileOutputContext varFileContext;
    private FileOutputContext funFileContext;
    private String varFileName = "VAR";
    private String funFileName = "FUN";
    private String separator = "\t";
    private List<? extends Chrome> solutionList;
    private List<Boolean> isObjectiveToBeMinimized;

    public SolutionListOutput(List<? extends Chrome> solutionList) {
        this.varFileContext = new DefaultFileOutputContext(this.varFileName);
        this.funFileContext = new DefaultFileOutputContext(this.funFileName);
        this.varFileContext.setSeparator(this.separator);
        this.funFileContext.setSeparator(this.separator);
        this.solutionList = solutionList;
        this.isObjectiveToBeMinimized = null;
    }

    public SolutionListOutput setVarFileOutputContext(FileOutputContext fileContext) {
        this.varFileContext = fileContext;
        return this;
    }

    public SolutionListOutput setFunFileOutputContext(FileOutputContext fileContext) {
        this.funFileContext = fileContext;
        return this;
    }

    public SolutionListOutput setObjectiveMinimizingObjectiveList(List<Boolean> isObjectiveToBeMinimized) {
        this.isObjectiveToBeMinimized = isObjectiveToBeMinimized;
        return this;
    }

    public SolutionListOutput setSeparator(String separator) {
        this.separator = separator;
        this.varFileContext.setSeparator(this.separator);
        this.funFileContext.setSeparator(this.separator);
        return this;
    }

    public void print() {
        if (this.isObjectiveToBeMinimized == null) {
            this.printObjectivesToFile(this.funFileContext, this.solutionList);
        } else {
            this.printObjectivesToFile(this.funFileContext, this.solutionList, this.isObjectiveToBeMinimized);
        }

        this.printVariablesToFile(this.varFileContext, this.solutionList);
    }

    public void printVariablesToFile(FileOutputContext context, List<? extends Chrome> solutionList) {
        BufferedWriter bufferedWriter = context.getFileWriter();

        try {
            if (solutionList.size() > 0) {
                int numberOfVariables = ((Chrome)solutionList.get(0)).getNumberOfVariables();

                for(int i = 0; i < solutionList.size(); ++i) {
                    for(int j = 0; j < numberOfVariables; ++j) {
                        bufferedWriter.write(((Chrome)solutionList.get(i)).getVariableValueString(j) + context.getSeparator());
                    }

                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.close();
        } catch (IOException var7) {
            throw new JMetalException("Error writing data ", var7);
        }
    }

    public void printObjectivesToFile(FileOutputContext context, List<? extends Chrome> solutionList) {
        BufferedWriter bufferedWriter = context.getFileWriter();

        try {
            if (solutionList.size() > 0) {
                int numberOfObjectives = ((Chrome)solutionList.get(0)).getNumberOfObjectives();

                for(int i = 0; i < solutionList.size(); ++i) {
                    for(int j = 0; j < numberOfObjectives; ++j) {
                        bufferedWriter.write(((Chrome)solutionList.get(i)).getObjective(j) + context.getSeparator());
                    }

                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.close();
        } catch (IOException var7) {
            throw new JMetalException("Error printing objecives to file: ", var7);
        }
    }

    public void printObjectivesToFile(FileOutputContext context, List<? extends Chrome> solutionList, List<Boolean> minimizeObjective) {
        BufferedWriter bufferedWriter = context.getFileWriter();

        try {
            if (solutionList.size() > 0) {
                int numberOfObjectives = ((Chrome)solutionList.get(0)).getNumberOfObjectives();
                if (numberOfObjectives != minimizeObjective.size()) {
                    throw new JMetalException("The size of list minimizeObjective is not correct: " + minimizeObjective.size());
                }

                for(int i = 0; i < solutionList.size(); ++i) {
                    for(int j = 0; j < numberOfObjectives; ++j) {
                        if ((Boolean)minimizeObjective.get(j)) {
                            bufferedWriter.write(((Chrome)solutionList.get(i)).getObjective(j) + context.getSeparator());
                        } else {
                            bufferedWriter.write(-1.0D * ((Chrome)solutionList.get(i)).getObjective(j) + context.getSeparator());
                        }
                    }

                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.close();
        } catch (IOException var8) {
            throw new JMetalException("Error printing objecives to file: ", var8);
        }
    }

    public void printObjectivesToFile(String fileName) throws IOException {
        this.printObjectivesToFile((FileOutputContext)(new DefaultFileOutputContext(fileName)), this.solutionList);
    }

    public void printObjectivesToFile(String fileName, List<Boolean> minimizeObjective) throws IOException {
        this.printObjectivesToFile(new DefaultFileOutputContext(fileName), this.solutionList, minimizeObjective);
    }

    public void printVariablesToFile(String fileName) throws IOException {
        this.printVariablesToFile(new DefaultFileOutputContext(fileName), this.solutionList);
    }
}
