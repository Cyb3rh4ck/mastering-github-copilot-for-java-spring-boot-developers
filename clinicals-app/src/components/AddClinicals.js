import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { toast } from 'react-toastify';
import axios from 'axios';
import './AddClinicals.css';

const AddClinicals = () => {
  const { patientId } = useParams(); // Obtener patientId de la URL
  const [patient, setPatient] = useState(null);
  const [clinicals, setClinicals] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [submitting, setSubmitting] = useState(false);
  
  // Estado para el formulario de datos clínicos
  const [clinicalData, setClinicalData] = useState({
    componentName: '',
    componentValue: ''
  });

  useEffect(() => {
    const fetchPatientDetails = async () => {
      if (!patientId) {
        setError('ID de paciente no encontrado');
        setLoading(false);
        return;
      }

      try {
        console.log('Fetching patient details for ID:', patientId);
        
        // Obtener detalles del paciente
        const patientResponse = await axios.get(`http://localhost:8080/patientservices/patients/${patientId}`);
        console.log('Patient details received:', patientResponse.data);
        setPatient(patientResponse.data);

        // Obtener datos clínicos del paciente
        try {
          const clinicalsResponse = await axios.get(`http://localhost:8080/patientservices/clinicals/patient/${patientId}`);
          console.log('Clinical data received:', clinicalsResponse.data);
          console.log('Clinical data type:', typeof clinicalsResponse.data);
          console.log('Is array?', Array.isArray(clinicalsResponse.data));
          
          // El endpoint ahora regresa un arreglo directamente
          if (Array.isArray(clinicalsResponse.data)) {
            setClinicals(clinicalsResponse.data);
          } else {
            console.warn('Expected array but received:', typeof clinicalsResponse.data);
            setClinicals([]);
          }
        } catch (clinicalsError) {
          console.log('No clinical data found or error fetching clinicals:', clinicalsError);
          setClinicals([]); // Si no hay datos clínicos, establecer array vacío
        }

        setError(null);
      } catch (error) {
        console.error('Error fetching patient details:', error);
        setError('Error al cargar los detalles del paciente');
        toast.error('Error al cargar los detalles del paciente');
      } finally {
        setLoading(false);
      }
    };

    fetchPatientDetails();
  }, [patientId]);

  // Handler para cambios en el formulario
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setClinicalData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Handler para limpiar el formulario
  const handleClearForm = () => {
    setClinicalData({
      componentName: '',
      componentValue: ''
    });
  };

  // Handler para enviar los datos clínicos
  const handleSubmit = async (e) => {
    e.preventDefault();
    setSubmitting(true);

    try {
      // Preparar los datos para enviar
      const clinicalPayload = {
        patientId: parseInt(patientId),
        componentName: clinicalData.componentName,
        componentValue: clinicalData.componentValue,
        measuredDateTime: new Date().toISOString() // Fecha y hora actual
      };

      console.log('Sending clinical data:', clinicalPayload);

      // Enviar PUT request al endpoint
      const response = await axios.put(
        `http://localhost:8080/patientservices/clinicals/${patientId}`,
        clinicalPayload,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );

      console.log('Clinical data saved:', response.data);

      // Mostrar mensaje de éxito
      toast.success('¡Dato clínico agregado exitosamente!', {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });

      // Limpiar el formulario
      handleClearForm();

      // Recargar los datos clínicos para mostrar el nuevo registro
      try {
        const clinicalsResponse = await axios.get(`http://localhost:8080/patientservices/clinicals/patient/${patientId}`);
        console.log('Updated clinical data received:', clinicalsResponse.data);
        
        // El endpoint ahora regresa un arreglo directamente
        if (Array.isArray(clinicalsResponse.data)) {
          setClinicals(clinicalsResponse.data);
        } else {
          console.warn('Expected array but received:', typeof clinicalsResponse.data);
          setClinicals([]);
        }
      } catch (clinicalsError) {
        console.log('Error updating clinicals list:', clinicalsError);
      }

      // Opcional: Redirigir al inicio después de un breve delay
      setTimeout(() => {
        window.location.href = '/';
      }, 2000);

    } catch (error) {
      console.error('Error saving clinical data:', error);
      
      // Mostrar mensaje de error específico
      const errorMessage = error.response?.data?.message || 
                          error.response?.data || 
                          'Error al agregar el dato clínico. Por favor, inténtalo de nuevo.';
      
      toast.error(errorMessage, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="add-clinicals-container">
        <div className="loading">Cargando detalles del paciente...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="add-clinicals-container">
        <div className="error">{error}</div>
      </div>
    );
  }

  if (!patient) {
    return (
      <div className="add-clinicals-container">
        <div className="no-patient">No se encontraron detalles del paciente.</div>
      </div>
    );
  }

  return (
    <div className="add-clinicals-container">
      <header className="header">
        <h1>Agregar Datos Clínicos</h1>
        <p>Paciente: {patient.firstName} {patient.lastName}</p>
      </header>
      
      <nav className="navigation">
        <button 
          className="btn btn-secondary" 
          onClick={() => window.location.href = '/'}
        >
          ← Volver al Inicio
        </button>
      </nav>
      
      <main className="main-content">
        <div className="patient-and-form">
          <div className="patient-info">
            <h3>Información del Paciente</h3>
            <div className="patient-details">
              <div className="detail-item">
                <span className="detail-label">ID:</span>
                <span>{patient.id}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Nombre:</span>
                <span>{patient.firstName}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Apellido:</span>
                <span>{patient.lastName}</span>
              </div>
              <div className="detail-item">
                <span className="detail-label">Edad:</span>
                <span>{patient.age} años</span>
              </div>
            </div>
          </div>

          <div className="form-container">
          <h3>Agregar Datos Clínicos</h3>
          <form className="clinical-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="componentName">Componente:</label>
              <select 
                id="componentName" 
                name="componentName" 
                value={clinicalData.componentName}
                onChange={handleInputChange}
                required
                disabled={submitting}
              >
                <option value="">Seleccionar componente</option>
                <option value="bp">Presión Arterial</option>
                <option value="heartrate">Frecuencia Cardíaca</option>
                <option value="temperature">Temperatura</option>
                <option value="weight">Peso</option>
                <option value="height">Altura</option>
                <option value="glucose">Glucosa</option>
              </select>
            </div>
            
            <div className="form-group">
              <label htmlFor="componentValue">Valor:</label>
              <input
                type="text"
                id="componentValue"
                name="componentValue"
                value={clinicalData.componentValue}
                onChange={handleInputChange}
                placeholder={
                  clinicalData.componentName === 'bp' ? 'Ej: 120/80' :
                  clinicalData.componentName === 'heartrate' ? 'Ej: 72' :
                  clinicalData.componentName === 'temperature' ? 'Ej: 36.5' :
                  clinicalData.componentName === 'weight' ? 'Ej: 70.5' :
                  clinicalData.componentName === 'height' ? 'Ej: 175' :
                  clinicalData.componentName === 'glucose' ? 'Ej: 95' :
                  'Ingresa el valor'
                }
                required
                disabled={submitting}
              />
            </div>
            
            <div className="form-actions">
              <button 
                type="submit" 
                className="btn btn-success"
                disabled={submitting}
              >
                {submitting ? 'Agregando...' : 'Agregar Dato Clínico'}
              </button>
              <button 
                type="button" 
                className="btn btn-outline"
                onClick={handleClearForm}
                disabled={submitting}
              >
                Limpiar
              </button>
            </div>
          </form>
        </div>
        </div>

        {/* Sección de datos clínicos existentes - Movida debajo */}
        <div className="clinicals-history">
          <h3>Historial de Datos Clínicos ({clinicals.length} registro{clinicals.length !== 1 ? 's' : ''})</h3>
          {clinicals.length > 0 ? (
            <div className="clinicals-table">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Componente</th>
                    <th>Valor</th>
                    <th>Fecha de Medición</th>
                  </tr>
                </thead>
                <tbody>
                  {clinicals
                    .sort((a, b) => new Date(b.measuredDateTime) - new Date(a.measuredDateTime)) // Ordenar por fecha descendente
                    .map((clinical, index) => {
                      console.log('Rendering clinical:', clinical, 'at index:', index);
                      return (
                        <tr key={clinical.id || index}>
                          <td>{clinical.id || 'N/A'}</td>
                          <td>
                            {clinical.componentName === 'bp' ? 'Presión Arterial' :
                             clinical.componentName === 'heartrate' ? 'Frecuencia Cardíaca' :
                             clinical.componentName === 'heart_rate' ? 'Frecuencia Cardíaca' :
                             clinical.componentName === 'temperature' ? 'Temperatura' :
                             clinical.componentName === 'weight' ? 'Peso' :
                             clinical.componentName === 'height' ? 'Altura' :
                             clinical.componentName === 'glucose' ? 'Glucosa' :
                             clinical.componentName === 'blood_pressure' ? 'Presión Arterial' :
                             clinical.componentName || 'N/A'}
                          </td>
                          <td>
                            {clinical.componentValue || 'N/A'}
                            {clinical.componentName === 'temperature' && clinical.componentValue ? ' °C' :
                             clinical.componentName === 'weight' && clinical.componentValue ? ' kg' :
                             clinical.componentName === 'height' && clinical.componentValue ? ' cm' :
                             clinical.componentName === 'glucose' && clinical.componentValue ? ' mg/dL' :
                             (clinical.componentName === 'heartrate' || clinical.componentName === 'heart_rate') && clinical.componentValue ? ' bpm' :
                             ''}
                          </td>
                          <td>
                            {clinical.measuredDateTime 
                              ? new Date(clinical.measuredDateTime).toLocaleString('es-ES', {
                                  year: 'numeric',
                                  month: '2-digit',
                                  day: '2-digit',
                                  hour: '2-digit',
                                  minute: '2-digit',
                                  second: '2-digit'
                                })
                              : 'No disponible'
                            }
                          </td>
                        </tr>
                      );
                    })}
                </tbody>
              </table>
            </div>
          ) : (
            <div className="no-clinicals">
              <p>No hay datos clínicos registrados para este paciente.</p>
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default AddClinicals;