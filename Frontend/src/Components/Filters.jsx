import React, { useState, useEffect } from 'react'
import FilterElement from './FilterElement';
import { getAllCategories, getAllCities } from '../service/ApiService';

const Filters = ({ applyFilters }) => {
    const [categories, setCategories] = useState([]);
    const [cities, setCities] = useState([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const categoryList = await getAllCategories();
                setCategories(categoryList);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        };

        fetchData();
    }, []);

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

    return (
        <div>
            <h2>Filter</h2>
            <FilterElement title='Cities' data={cities} />
            <FilterElement title='Categories' data={categories} />
            <button onClick={applyFilters}>Apply</button>
        </div>
    )
}

export default Filters;
