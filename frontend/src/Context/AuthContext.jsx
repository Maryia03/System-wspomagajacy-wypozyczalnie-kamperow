import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie'

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);  // Stan dla aktualnego użytkownika
    const [errorMessage, setErrorMessage] = useState("")
    const [users, setUsers] = useState([]);  // Stan dla wszystkich użytkowników
    const [reservations, setReservations] = useState([]); // Stan dla rezerwacji
    const [notifications, setNotifications] = useState([]);

    const config = {
        headers: { 'Authorization': `Bearer ${Cookies.get("user_key")}` }
    };

    // Pobieranie danych z localStorage przy uruchomieniu
    useEffect(() => {
        const storedUser = JSON.parse(localStorage.getItem('currentUser'));
        if (storedUser) {
            setCurrentUser(storedUser);
        }


        // Pobieramy użytkowników z localStorage
        const storedUsers = JSON.parse(localStorage.getItem('users')) || [];
        const updatedUsers = storedUsers.map(user => {
            if (!user.registeredAt) {
                user.registeredAt = new Date().toISOString();
            }
            return user;
        });

        setUsers(updatedUsers);
        localStorage.setItem('users', JSON.stringify(updatedUsers));

        if (storedUser) {
            // const storedReservations = JSON.parse(localStorage.getItem('reservations')) || [];
            // const userReservations = storedReservations.filter(reservation => reservation.userId === storedUser.id);
            // setReservations(userReservations);
            
        }

        if(Cookies.get("admin") == '1'){
            setCurrentUser({role: 'admin'})
        }

        const storedInsurances = JSON.parse(localStorage.getItem('insurances')) || [];
        setInsurances(storedInsurances);
    }, []);

    // Funkcja do logowania użytkownika
    const login = (user, password) => {
        var data = { email: user, password: password }
        // console.log("login: " + JSON.stringify(data))
        axios.post("http://localhost:8080/auth/login", data
        ).then((res) => {
            console.log((res));
            if(res.data == "invalid user data"){
                alert(res.data)
                return ;
            }
            // document.cookie = 'user_key=' + res.data.token + ';expires=' + new Date(new Date().getTime() + 3600000).toGMTString() + ';'
            var thirtyMinutes = new Date(new Date().getTime() + 30 * 60 * 1000);
            Cookies.set("user_id",res.data.id,{expires: thirtyMinutes})
            Cookies.set("user_key",res.data.token,{expires: thirtyMinutes})
            if(res.data.admin == true){
                Cookies.set("admin","1",{expires: thirtyMinutes})
            }
            setErrorMessage('')
            window.location.reload();
        }).catch(function (error) {
            console.log(error);
            setErrorMessage("Nieprawidłowe dane logowania")
            console.log(errorMessage)
            return false;
        });



        // document.cookie = 'user_key=' + user + ";";
        // setCurrentUser(user);
        // localStorage.setItem('currentUser', JSON.stringify(user)); // Zapisz użytkownika w localStorage

        // // Ładowanie rezerwacji tylko dla zalogowanego użytkownika
        // const storedReservations = JSON.parse(localStorage.getItem('reservations')) || [];
        // const userReservations = storedReservations.filter(reservation => reservation.userId === user.id);
        // setReservations(userReservations);
    };

    const logout = () => {
        setReservations([]);
        Cookies.remove("user_key")
        Cookies.remove("user_id")
        Cookies.remove("admin")
        // localStorage.removeItem('currentUser'); // Usuwamy użytkownika z localStorage
        // localStorage.removeItem('reservations'); // Usuwamy rezerwacje z localStorage
    };

    const register = (newUser) => {

        var data = {
            email: newUser.email,
            username: newUser.firstName + " " + newUser.lastName,
            password: newUser.password,
            address: newUser.address,
            phone: newUser.phoneNumber
        }

        console.log(data)
        axios.post("http://localhost:8080/auth/register", data)
            .then((res) => {
                console.log(res)
                login(data.email, data.password);
            }).catch((err) => {
                console.log(err)
            })



        // const isUserExist = users.some(user => user.email === newUser.email);
        // if (isUserExist) {
        //     alert("Użytkownik z tym emailem już istnieje.");
        //     return;
        // }

        // const updatedUsers = [...users, newUser];
        // setUsers(updatedUsers);
        // localStorage.setItem('users', JSON.stringify(updatedUsers));
        // login(newUser);
    };

    const addReservation = (newReservation) => {

        
        if (Cookies.get("user_key") == "undefined"  || !Cookies.get("user_key")) {
            alert("Musisz być zalogowany, aby dodać rezerwację.");
            return;
        }
        const config = {
            headers: { 'Authorization': `Bearer ${Cookies.get("user_key")}` }
        };
        var data = {

            vehicleId: newReservation.vehicleId,
            reservationStartDate: (newReservation.reservationStartDate),
            reservationEndDate: (newReservation.reservationEndDate),
            comments: "string",
            location: newReservation.location

        }
        console.log("auth:")
        console.log(newReservation)
        console.log(data)
        axios.post("http://localhost:8080/reservation", data, config)
            .then((res) => {
                console.log(res)
                if(res.data != "Success")
                    alert(res.data)
            }).catch((err) => {
                console.log(err)
            })


        // newReservation.userId = currentUser.id; // Przypisujemy rezerwację do użytkownika

        // setReservations((prevReservations) => {
        //     const updatedReservations = [...prevReservations, newReservation];
        //     const allReservations = JSON.parse(localStorage.getItem('reservations')) || [];
        //     allReservations.push(newReservation); // Dodajemy rezerwację do globalnego zbioru
        //     localStorage.setItem('reservations', JSON.stringify(allReservations)); // Zapisujemy do localStorage
        //     return updatedReservations;
        // });

    };

    // Funkcja do usuwania rezerwacji
    const removeReservation = (reservationId) => {
        axios.delete(`http://localhost:8080/reservation/${reservationId}`,config)
        .then((res)=>{
            console.log(res)
        })
        .catch((err)=>{
            console.log(err)
            alert(err)
        })
    };

    const getAllUsers = async () =>{
        console.log("get all users");
        const config = {
            headers: { 'Authorization': `Bearer ${Cookies.get("user_key")}` }
        };
        return axios.get("http://localhost:8080/user/all",config)
        .then((res)=>{
            console.log(res.data);
            return res.data;
        })
        .catch((err)=>{
            console.log(err)
            alert(err)
        })

    }

    const updateUserRole = ()=>{
        console.log("update user role")
    }

    const toggleBlockUser = async (userId) => {
        console.log("toggle block user")
        return axios.get("http://localhost:8080/user/block/"+userId,config)
        .then((res)=>{
            console.log(res)
        })
        .catch((err)=>{
            console.log(err)
            alert(err)
        })
    }

    const getReservationsByUserId = async (id) =>{
        console.log("get reservations by user id")
        if (id==undefined){
           return axios.get("http://localhost:8080/reservation/all",config)
            .then((res)=>{
                console.log(res.data)
                return res.data
            })
            .catch((err)=>{
                console.log(err)
                alert(err)
            })
        }
    }

    const getVehicleReservations = async (id) =>{
        console.log("get reservations by vehicle id")
        return axios.get("http://localhost:8080/reservation/vehicle/"+id,config)
            .then((res)=>{
                console.log(res.data)
                return res.data
            })
            .catch((err)=>{
                console.log(err)
                alert(err)
            })
    }

    const acceptReservation = async (id) => {
        console.log("accept reservation"+id)

        return axios.get(`http://localhost:8080/reservation/accept/${id}`,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const getAllInsurances = async () =>{
        console.log("get insurance")
        return axios.get("http://localhost:8080/inspection/all",config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const addInsurance = async (insuranceData) =>{
        console.log("add insurance")
        axios.post('http://localhost:8080/inspection/create',insuranceData,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const updateInsurance = async (data) => {
        console.log("update insuracne")
        axios.patch('http://localhost:8080/inspection/update/'+data.id,data,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const deleteUser = (userId) =>{
        console.log("delete user")

        axios.delete(`http://localhost:8080/user/delete?id=${userId}`,config)
        .then((res)=>{
            console.log(res.data)
        }).catch((err)=>{
            alert(err)
            console.log(err)
        })
    }

    const cancelReservation = async(id) =>{
        console.log("cancel reservation")

        return axios.get(`http://localhost:8080/reservation/cancel/${id}`,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const resignReservation = async(id) =>{
        console.log("resign reservation")

        return axios.get(`http://localhost:8080/reservation/resign/${id}`,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }
    const getCamperById = () =>{
        console.log("get camper by id")
    }
    const getAllCampers = async ()=>{
        console.log("all campers")

        return axios.get(`http://localhost:8080/vehicle/all`,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const getAllPrices = async ()=>{
        console.log("all prices")

        return axios.get(`http://localhost:8080/prices/all`,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const addPrice = async (priceData) => {
        console.log("add prices")

        return axios.post(`http://localhost:8080/prices/add`,priceData,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
        
    }
    const getReservations = () => {
        console.log("get reservations")
    }


    const addCamper = (camperData) =>{
        console.log("add camper")
        axios.post("http://localhost:8080/vehicle/add",camperData,config)
        .then((res)=>{
            console.log(res)
        })
        .catch((err)=>{
            alert(err)
            console.log(err)
        })
    }

    const deleteCamper = (camperId) =>{
        console.log("delete camper: "+camperId)
        axios.delete(`http://localhost:8080/vehicle/delete/${camperId}`,config)
        .then((res)=>{
            console.log(res)
        })
        .catch((err)=>{
            alert(err)
            console.log(err)
        })
    }

    const updateCamper = (camperData) =>{
        console.log("update camper")
        console.log(camperData)

        axios.patch(`http://localhost:8080/vehicle/update/${camperData.id}`,camperData,config)
        .then((res)=>{
            console.log(res)
        })
        .catch((err)=>{
            alert(err)
            console.log(err)
        })
    }

    const setInsurances = () =>{
        console.log("set insurances")
    }

    const createPickupReport = async(report)=>{
        console.log("create pickup report")
        return axios.post("http://localhost:8080/reports/pickup",report,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    
    const createReturnReport = async(report)=>{
        console.log("create pickup report")
        return axios.post("http://localhost:8080/reports/return",report,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const getAllReports = async () => {
        console.log("get all reports")
        return axios.get("http://localhost:8080/reports",config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const getAllRepairs = async () =>{
        console.log("get all repairs")
        return axios.get("http://localhost:8080/repairs",config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const addRepair = async(newRepair) =>{
        console.log("add Repair")
        return axios.post("http://localhost:8080/repairs",newRepair,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const deleteRepair = async(id) =>{
        console.log("add Repair")
        return axios.delete("http://localhost:8080/repairs/"+id,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const updateRepair = async(repairData) =>{
        console.log("add Repair")
        return axios.put("http://localhost:8080/repairs/"+repairData.id,repairData,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const addLocation = async(locationData) =>{
        console.log("add location")
        return axios.post("http://localhost:8080/location",locationData,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })

    }

    const deleteLocation = async(id) =>{
        return axios.delete("http://localhost:8080/location/"+id,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
        
    }

    const getAllLocations = async()=>{
        return axios.get("http://localhost:8080/location/all",config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })
    }

    const updateLocation = async(id,data)=>{
        return axios.patch("http://localhost:8080/location/"+id,data,config)
        .then((res)=>{
            console.log(res)
            return res.data
        })
        .catch((err)=>{
            alert(err)
            return err
        })

    }

    



    return (
        <AuthContext.Provider value={{
            users,
            reservations,
            notifications,
            login,
            logout,
            register,
            getAllUsers,
            updateUserRole,
            toggleBlockUser,
            addReservation,
            getReservationsByUserId,
            acceptReservation,
            deleteUser,
            removeReservation,
            cancelReservation,
            resignReservation,
            getAllInsurances,
            addInsurance,
            updateInsurance,
            getCamperById,
            getAllCampers,
            addCamper,
            deleteCamper,
            updateCamper,
            errorMessage,
            addReservation,
            removeReservation,
            getReservations,
            getVehicleReservations,
            getAllPrices,
            addPrice,
            createPickupReport,
            createReturnReport,
            getAllReports,
            getAllRepairs,
            addRepair,
            deleteRepair,
            updateRepair,
            addLocation,
            deleteLocation,
            getAllLocations,
            updateLocation,
            login,
            logout,
            register
        }}>
            {children}
        </AuthContext.Provider>
    );
};
