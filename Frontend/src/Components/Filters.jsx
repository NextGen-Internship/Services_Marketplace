import React, { useState, useEffect } from 'react'
import FilterElement from './FilterElement';
import { getAllCategories, getAllCities } from '../service/ApiService';
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';

const Filters = ({ applyFilters }) => {
    const [categories, setCategories] = useState([]);
    const [cities, setCities] = useState([]);
    const [priceRange, setPriceRange] = useState([0, 1000]);

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

    const handlePriceRangeChange = (value) => {
        setPriceRange(value);
    };

    return (
        <div>
            <h2>Filter</h2>
            <FilterElement title='Cities' data={cities} />
            <FilterElement title='Categories' data={categories} />
            <label>
                Price Range: {priceRange[0]} - {priceRange[1]}
                <Slider
                    min={0}
                    max={1000}
                    range
                    defaultValue={priceRange}
                    onChange={handlePriceRangeChange}
                />
            </label>
            <button /*onClick={applyFilters}*/>Apply</button>
        </div>
    )
}

export default Filters;
