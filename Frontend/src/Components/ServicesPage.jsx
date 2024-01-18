import React from 'react';
import ServicesPageHeader from './ServicesPageHeader';
import ServiceBoxes from './ServiceBoxes';
import { useState, useEffect } from 'react';
import { getAllServices, getPaginationServices } from '../service/ApiService';
import Filters from './Filters';
import '../styles/ServicesPage.css'

const ServicesPage = () => {
    const [services, setServices] = useState([]);
    const [page, setPage] = useState(0);
    const [pageSize, setPageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(0);
    const [totalElements, setTotalElements] = useState(0);
    const [sortingField, setSortingField] = useState('updatedAt');
    const [sortingDirection, setSortingDirection] = useState('DESC');
    const [currentPage, setCurrentPage] = useState(0);

    // useEffect(() => {
    //     const fetchData = async () => {
    //       try {
    //         const servicesList = await getAllServices();
    //         setServices(servicesList);
    //       } catch (error) {
    //         console.error('Error fetching data:', error);
    //       }
    //     };

    //     fetchData();
    //   }, []);

    const getServices = async (page, sortingField, sortingDirection) => {
        try {
            const response = await getPaginationServices(page, pageSize, sortingField, sortingDirection);
            setServices(response.content);
            setTotalPages(response.totalPages);
            setTotalElements(response.totalElements);
            setCurrentPage(response.number);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    const displayPageable = () => {
        const previousPage = Math.max(page - 1, 0);
        const nextPage = Math.min(page + 1, totalPages - 1);
    
        return (
            <div>
                <button onClick={() => getServices(previousPage, sortingField, sortingDirection)}>Previous</button>
    
                {[...Array(totalPages)].map((_, index) => (
                    <button
                        key={index}
                        onClick={() => getServices(index, sortingField, sortingDirection)}
                        className={index === currentPage ? 'active' : ''}
                    >
                        {index + 1}
                    </button>
                ))}
    
                <button
                    onClick={() => getServices(nextPage, sortingField, sortingDirection)}
                    disabled={page === totalPages - 1} // Disable if on the last page
                >
                    Next
                </button>
                <br />
                <span>{(((currentPage + 1) * pageSize) > totalElements) ? totalElements : ((currentPage + 1) * pageSize)} of {totalElements} elements</span>
            </div>
        );
    };
    

    const sort = (field) => {
        const newSortingDirection = sortingDirection === 'ASC' ? 'DESC' : 'ASC';
        setSortingDirection(newSortingDirection);
        getServices(page, field, newSortingDirection);
    };

    useEffect(() => {
        getServices(page, sortingField, sortingDirection);
    }, [page, pageSize, sortingField, sortingDirection]);



    return (
        <div className='page-container'>
            <div className='filter'>
                <Filters />
            </div>
            <div className='services'>
                <ServicesPageHeader />
                {services.length > 0 ? <ServiceBoxes services={services} /> : 'No Services to Show'}
                {displayPageable()}
            </div>
        </div>
    )
}

export default ServicesPage;
