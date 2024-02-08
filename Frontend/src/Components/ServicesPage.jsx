import React, { useState, useEffect } from 'react';
import ReactPaginate from 'react-paginate';
import ServicesPageHeader from './ServicesPageHeader';
import ServiceBoxes from './ServiceBoxes';
import { getPaginationServices, getPaginationFilteredServices, getAllCities, getAllCategories } from '../service/ApiService';
import Filters from './Filters';
import '../styles/ServicesPage.css';


const ServicesPage = () => {
    const isNavigationEvent = !sessionStorage.getItem('PageNumber');
    const storedPage = isNavigationEvent ? 0 : sessionStorage.getItem('PageNumber');

    const [services, setServices] = useState([]);
    const [page, setPage] = useState(Number(storedPage));
    const [pageSize, setPageSize] = useState(5);
    const [totalPages, setTotalPages] = useState(0);
    const [selectedService, setSelectedService] = useState(null);
    const [totalElements, setTotalElements] = useState(0);
    const [sortingField, setSortingField] = useState('updatedAt');
    const [sortingDirection, setSortingDirection] = useState('DESC');
    const [cities, setCities] = useState([]);
    const [categories, setCategories] = useState([]);


    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await getAllCities();

                const citiesWithLabel = response.map((city) => ({
                    id: city.id,
                    name: city.name,
                }));

                setCities(citiesWithLabel);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const categoryList = await getAllCategories();

                const categoriesWithLabel = categoryList.map((category) => ({
                    id: category.id,
                    name: category.name,
                }));

                setCategories(categoriesWithLabel);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);


    const getServices = async (page, sortingField, sortingDirection) => {
        try {
            const response = await getPaginationServices(page, pageSize, sortingField, sortingDirection);
            setServices(response.content);
            setTotalPages(response.totalPages);
            setTotalElements(response.totalElements);
            setPage(response.number);

            sessionStorage.setItem('PageNumber', response.number);
        } catch (error) {
            console.error('Error fetching users:', error);
        }
    };

    useEffect(() => {
        getServices(page, sortingField, sortingDirection);
    }, [sortingField, sortingDirection]);

    const handlePageChange = ({ selected }) => {
        setPage(selected);
    };

    useEffect(() => {
        getServices(page, sortingField, sortingDirection);
    }, [page, pageSize, sortingField, sortingDirection]);

    const getFilteredServices = async (serviceFilterRequest) => {
        try {
            const response = await getPaginationFilteredServices(serviceFilterRequest);
            setServices(response.content);
            setTotalPages(response.totalPages);
            setTotalElements(response.totalElements);
            setPage(response.number);

            sessionStorage.setItem('PageNumber', response.number);
            sessionStorage.setItem('FilterParams', JSON.stringify(serviceFilterRequest));
        } catch (error) {
            console.error('Error fetching services:', error);
        }
    }

    return (
        <div className='page-container'>
            <div className='filter'>
                <Filters applyFilters={getFilteredServices} cities={cities} categories={categories} />
            </div>
            <div className='services'>
                {services.length > 0 ? <ServiceBoxes services={services} cities={cities} />
                    : 'No Services to Show'}
                <ReactPaginate
                    pageCount={totalPages}
                    pageRangeDisplayed={2}
                    marginPagesDisplayed={1}
                    onPageChange={handlePageChange}
                    containerClassName="pagination"
                    activeClassName="active"
                    initialPage={page}
                />
            </div>
        </div>
    );
};
export default ServicesPage;
