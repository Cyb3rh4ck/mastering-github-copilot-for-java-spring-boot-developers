import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'react-toastify';
import './AddPatient.css';

const AddPatient = () => {
  const [patient, setPatient] = useState({
    firstName: '',
    lastName: '',
    age: ''
  });
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPatient({ ...patient, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      const response = await axios.post('http://localhost:8080/patientservices/patients', patient);
      
      // Mostrar toast de éxito
      toast.success('¡Paciente agregado exitosamente!', {
        position: "top-right",
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
      
      console.log(response.data);
      setPatient({ firstName: '', lastName: '', age: '' });
      
      // Opcional: Redirigir al inicio después de un breve delay
      setTimeout(() => {
        window.location.href = '/';
      }, 2000);
      
    } catch (error) {
      console.error('Error adding patient:', error);
      
      // Mostrar toast de error
      toast.error('Error al agregar el paciente. Por favor, inténtalo de nuevo.', {
        position: "top-right",
        autoClose: 5000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="add-patient-container">
      <header className="header">
        <h1>Agregar Nuevo Paciente</h1>
        <p>Completa la información del paciente</p>
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
        <div className="form-container">
          <form onSubmit={handleSubmit} className="patient-form">
            <div className="form-group">
              <label htmlFor="firstName">Nombre:</label>
              <input
                type="text"
                id="firstName"
                name="firstName"
                value={patient.firstName}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Ingresa el nombre"
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="lastName">Apellido:</label>
              <input
                type="text"
                id="lastName"
                name="lastName"
                value={patient.lastName}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Ingresa el apellido"
              />
            </div>
            
            <div className="form-group">
              <label htmlFor="age">Edad:</label>
              <input
                type="number"
                id="age"
                name="age"
                value={patient.age}
                onChange={handleChange}
                required
                disabled={loading}
                placeholder="Ingresa la edad"
                min="0"
                max="150"
              />
            </div>
            
            <div className="form-actions">
              <button 
                type="submit" 
                className="btn btn-success"
                disabled={loading}
              >
                {loading ? 'Agregando...' : '+ Agregar Paciente'}
              </button>
              
              <button 
                type="button" 
                className="btn btn-outline"
                onClick={() => setPatient({ firstName: '', lastName: '', age: '' })}
                disabled={loading}
              >
                Limpiar Formulario
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
};

export default AddPatient;