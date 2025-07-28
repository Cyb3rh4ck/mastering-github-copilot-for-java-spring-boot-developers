import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import './Home.css';

function PatientTable() {
  const [patients, setPatients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchPatients();
  }, []);

  const fetchPatients = () => {
    console.log('Fetching patients from:', 'http://localhost:8080/patientservices/patients');
    axios.get('http://localhost:8080/patientservices/patients')
      .then(response => {
        console.log('Patients received:', response.data);
        setPatients(response.data);
        setLoading(false);
      })
      .catch(error => {
        console.error('Error fetching patient data:', error);
        console.error('Error details:', {
          message: error.message,
          response: error.response?.data,
          status: error.response?.status
        });
        
        // Mostrar toast de error en lugar de estado interno
        toast.error('Error al cargar los datos de pacientes. Verifique que el servidor esté ejecutándose.', {
          position: "top-right",
          autoClose: 5000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
        });
        
        setError(`Error al cargar los datos de pacientes: ${error.message}`);
        setLoading(false);
      });
  };

  const handleDeletePatient = async (patientId, patientName) => {
    // Confirmar eliminación
    if (!window.confirm(`¿Está seguro de que desea eliminar al paciente ${patientName}?`)) {
      return;
    }

    try {
      console.log('Deleting patient with ID:', patientId);
      
      await axios.delete(`http://localhost:8080/patientservices/patients/${patientId}`);
      
      // Mostrar toast de éxito
      toast.success(`¡Paciente ${patientName} eliminado exitosamente!`, {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });

      // Recargar la lista de pacientes
      fetchPatients();
      
    } catch (error) {
      console.error('Error deleting patient:', error);
      
      // Mostrar mensaje de error específico
      const errorMessage = error.response?.data?.message || 
                          error.response?.data || 
                          'Error al eliminar el paciente. Por favor, inténtalo de nuevo.';
      
      toast.error(errorMessage, {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
    }
  };

  if (loading) {
    return <div className="loading">Cargando pacientes...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  if (patients.length === 0) {
    return <div className="no-patients">No hay pacientes registrados</div>;
  }

  return (
    <div className="table-container">
      <table className="patients-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Edad</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {patients.map(patient => (
            <tr key={patient.id}>
              <td>{patient.id}</td>
              <td>{patient.firstName}</td>
              <td>{patient.lastName}</td>
              <td>{patient.age}</td>
              <td className="actions-cell">
                <button 
                  className="btn btn-primary action-btn" 
                  onClick={() => window.location.href = `/add-clinical/${patient.id}`}
                >
                  Agregar Clínico
                </button>
                <button 
                  className="btn btn-danger action-btn" 
                  onClick={() => handleDeletePatient(patient.id, `${patient.firstName} ${patient.lastName}`)}
                >
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function Home() {
  return (
    <div className="home-container">
      <header className="header">
        <h1>Sistema de Gestión de Pacientes</h1>
        <p>Administra pacientes y sus datos clínicos</p>
      </header>
      
      <nav className="navigation">
        <button 
          className="btn btn-success nav-btn" 
          onClick={() => window.location.href = '/add-patient'}
        >
          + Agregar Nuevo Paciente
        </button>
      </nav>
      
      <main className="main-content">
        <h2>Lista de Pacientes</h2>
        <PatientTable />
      </main>
    </div>
  );
}

export default Home;
