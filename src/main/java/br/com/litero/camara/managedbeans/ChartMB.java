package br.com.litero.camara.managedbeans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

import br.com.litero.camara.model.StatusCaso;
import br.com.litero.camara.model.TipoParte;
import br.com.litero.camara.repositorios.CasoRepository;
import br.com.litero.camara.service.ParteService;
 
 
@Named
@javax.faces.view.ViewScoped
public class ChartMB implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private PieChartModel pieModel1;
    private LineChartModel lineModel1;
    
    @Inject
	private CasoRepository casoDAO;
    
    @Inject
   	private ParteService parteService;
 
    @PostConstruct
    public void init() {
        createPieModels();
        createLineModels();
    }
    
    public LineChartModel getLineModel1() {
        return lineModel1;
    }
     
    public PieChartModel getPieModel1() {
        return pieModel1;
    }
 
    private void createPieModels() {
        createPieModel1();
    }
     
    private void createPieModel1() {
        pieModel1 = new PieChartModel();

        pieModel1.set(StatusCaso.EM_ANALISE.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.EM_ANALISE));
        pieModel1.set(StatusCaso.CASO_ACEITO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.CASO_ACEITO));
        pieModel1.set(StatusCaso.PROCESSO_COMUNICACAO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.PROCESSO_COMUNICACAO));
        pieModel1.set(StatusCaso.TRAMITANDO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.TRAMITANDO));
        pieModel1.set(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR));
        pieModel1.set(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.AGUARDANDO_VALIDACAO_MEDIADOR));
        pieModel1.set(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.AGUARDANDO_DEFINICAO_MEDIADOR_CAMARA));
        pieModel1.set(StatusCaso.AGUARDANDO_ACEITE_CONVIDADO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.AGUARDANDO_ACEITE_CONVIDADO));
        pieModel1.set(StatusCaso.NEGADO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.NEGADO));
        pieModel1.set(StatusCaso.IMPASSE.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.IMPASSE));
        pieModel1.set(StatusCaso.ENCERRADO.getDescricao(), casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.ENCERRADO));
         
        pieModel1.setTitle("Situação");
        pieModel1.setLegendPosition("w");
    }
    
    private void createLineModels() {
    	
        lineModel1 = initLinearModel();
        
       
        
        lineModel1.setTitle("Linear Chart");
        lineModel1.setLegendPosition("e");
        Axis yAxis = lineModel1.getAxis(AxisType.Y);
        yAxis.setMin(0);
        yAxis.setMax(10);
        
       
       
    }
     
    private LineChartModel initLinearModel() {
        LineChartModel model = new LineChartModel();
       
 
        LineChartSeries series1 = new LineChartSeries();
        series1.setLabel("Series 1");
 
        series1.set(1, 2);
        series1.set(2, 1);
        series1.set(3, 3);
        series1.set(4, 6);
        series1.set(5, 8);
 
        LineChartSeries series2 = new LineChartSeries();
        series2.setLabel("Series 2");
 
        series2.set(1, 6);
        series2.set(2, 3);
        series2.set(3, 2);
        series2.set(4, 7);
        series2.set(5, 9);
 
        model.addSeries(series1);
        model.addSeries(series2);
         
        return model;
    }
 
    public void itemSelect(ItemSelectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Item selected",
                        "Item Index: " + event.getItemIndex() + ", Series Index:" + event.getSeriesIndex());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

	public long getQtdTotalCasos() {
		return casoDAO.recuperarQuantidadeCaso();
	}

	public long getQtdRequerentes() {
		return parteService.recuperarQuantidadePorTipoParte(TipoParte.REQUERENTE);
	}

	public long getQtdRequeridos() {
		return parteService.recuperarQuantidadePorTipoParte(TipoParte.REQUERIDO);
	}
	
	public long getQtdCasosTramitando() {
		return casoDAO.recuperarQuantidadeCasoPorStatus(StatusCaso.TRAMITANDO);
	}
	
	
	
	
    
    
    
    
}
