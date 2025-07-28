import { toast } from 'react-toastify';

// Configuraciones predefinidas para diferentes tipos de notificaciones
const defaultConfig = {
  position: "top-right",
  hideProgressBar: false,
  closeOnClick: true,
  pauseOnHover: true,
  draggable: true,
};

export const showSuccess = (message, config = {}) => {
  toast.success(message, {
    ...defaultConfig,
    autoClose: 3000,
    ...config
  });
};

export const showError = (message, config = {}) => {
  toast.error(message, {
    ...defaultConfig,
    autoClose: 5000,
    ...config
  });
};

export const showInfo = (message, config = {}) => {
  toast.info(message, {
    ...defaultConfig,
    autoClose: 4000,
    ...config
  });
};

export const showWarning = (message, config = {}) => {
  toast.warning(message, {
    ...defaultConfig,
    autoClose: 4000,
    ...config
  });
};

// Función para limpiar todos los toasts
export const dismissAll = () => {
  toast.dismiss();
};

// Toast para confirmaciones
export const showConfirmation = (message, onConfirm, onCancel) => {
  toast(({ closeToast }) => (
    <div>
      <p>{message}</p>
      <div style={{ display: 'flex', gap: '10px', marginTop: '10px' }}>
        <button 
          style={{
            background: '#28a745',
            color: 'white',
            border: 'none',
            padding: '5px 15px',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
          onClick={() => {
            onConfirm();
            closeToast();
          }}
        >
          Sí
        </button>
        <button 
          style={{
            background: '#dc3545',
            color: 'white',
            border: 'none',
            padding: '5px 15px',
            borderRadius: '4px',
            cursor: 'pointer'
          }}
          onClick={() => {
            onCancel();
            closeToast();
          }}
        >
          No
        </button>
      </div>
    </div>
  ), {
    ...defaultConfig,
    autoClose: false,
    closeButton: false,
  });
};
