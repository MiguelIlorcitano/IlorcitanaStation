/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporte;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Usuario
 */
public class ActaDataSource implements JRDataSource {
    
    private final List<ActaMantenimiento> listaActas = new ArrayList<>();
    private int indiceActas = -1;
    
    ActaMantenimiento almacen = new ActaMantenimiento();
    int contador=0;

    @Override
    public boolean next() throws JRException {
        return ++indiceActas < listaActas.size();
        //return (contador<9);
    }
    
    public void AddActa(ActaMantenimiento almacen){
        this.listaActas.add(almacen);
    }
    

    @Override
    public Object getFieldValue(JRField jrf) throws JRException {
        Object valor = null;
        
        if (null != jrf.getName())switch (jrf.getName()) {
            case "Maquina":
                //valor = almacen.getMaquina();
                valor = listaActas.get(indiceActas).getMaquina();
                //contador++;
                break;
            case "Fecha":
                valor = listaActas.get(indiceActas).getFecha();
                //contador++;
                break;
            case "Operario":
                valor = listaActas.get(indiceActas).getOperario();
                //contador++;
                break;
            case "Tiempo empleado":
                valor = listaActas.get(indiceActas).getTiempo_empleado();
                //contador++;
                break;
            case "Tipo de mantenimiento":
                valor = listaActas.get(indiceActas).getTipo_mantenimiento();
                //contador++;
                break;
            case "Tipo de problema":
                valor = listaActas.get(indiceActas).getTipo_problema();
                //contador++;
                break;
            case "Operaciones":
                valor = listaActas.get(indiceActas).getOperaciones();
                //contador++;
                break;
            case "Piezas sustituidas":
                valor = listaActas.get(indiceActas).getPiezas_sustituidas();
                //contador++;
                break;
            case "Observaciones":
                valor = listaActas.get(indiceActas).getObservaciones();
                //contador++;
                break;
            default:
                break;
        }
        
        return valor;
    }
    
}
