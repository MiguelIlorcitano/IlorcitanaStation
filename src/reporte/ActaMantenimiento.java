/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reporte;

/**
 *
 * @author Usuario
 */
public class ActaMantenimiento {

    public String Maquina;
    public String Fecha;
    public String Operario;
    public String Tiempo_empleado;
    public String Tipo_mantenimiento;
    public String Tipo_problema;
    public String Operaciones;
    public String Piezas_sustituidas;
    public String Observaciones;
    
    public ActaMantenimiento() {
        
    }
    
    public ActaMantenimiento(String Maquina,String Fecha,String Operario,String Tiempo_empleado,String Tipo_mantenimiento,String Tipo_problema,String Operaciones,String Piezas_sustituidas,String Observaciones) {
        this.Maquina = Maquina;
        this.Fecha=Fecha;
        this.Operario=Operario;
        this.Tiempo_empleado=Tiempo_empleado;
        this.Tipo_mantenimiento=Tipo_mantenimiento;
        this.Tipo_problema=Tipo_problema;
        this.Operaciones=Operaciones;
        this.Piezas_sustituidas=Piezas_sustituidas;
        this.Observaciones=Observaciones;
    }

    public String getMaquina() {
        return Maquina;
    }

    public String getFecha() {
        return Fecha;
    }

    public String getOperario() {
        return Operario;
    }

    public String getTiempo_empleado() {
        return Tiempo_empleado;
    }

    public String getTipo_mantenimiento() {
        return Tipo_mantenimiento;
    }

    public String getTipo_problema() {
        return Tipo_problema;
    }

    public String getOperaciones() {
        return Operaciones;
    }

    public String getPiezas_sustituidas() {
        return Piezas_sustituidas;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setMaquina(String Maquina) {
        this.Maquina = Maquina;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public void setOperario(String Operario) {
        this.Operario = Operario;
    }

    public void setTiempo_empleado(String Tiempo_empleado) {
        this.Tiempo_empleado = Tiempo_empleado;
    }

    public void setTipo_mantenimiento(String Tipo_matenimiento) {
        this.Tipo_mantenimiento = Tipo_matenimiento;
    }

    public void setTipo_problema(String Tipo_problema) {
        this.Tipo_problema = Tipo_problema;
    }

    public void setOperaciones(String Operaciones) {
        this.Operaciones = Operaciones;
    }

    public void setPiezas_sustituidas(String Piezas_sustuidas) {
        this.Piezas_sustituidas = Piezas_sustuidas;
    }

    public void setObservaciones(String Observaciones) {
        this.Observaciones = Observaciones;
    }
    
    
}
