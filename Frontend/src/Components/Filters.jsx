import React, { useState, useEffect } from 'react';
import FilterElement from './FilterElement';
import { getAllCategories, getAllCities } from '../service/ApiService';
import Slider from 'rc-slider';
import 'rc-slider/assets/index.css';

const Filters = ({ applyFilters }) => {
    const [categories, setCategories] = useState([]);
    const [cities, setCities] = useState([]);
    const [priceRange, setPriceRange] = useState([0, 1000]);
    const [selectedCities, setSelectedCities] = useState([]);
    const [selectedCategories, setSelectedCategories] = useState([]);

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

    const handleApply = () => {
        const categories = selectedCategories || [];
        const cities = selectedCities || [];

        const categoryIds = categories.map(category => category.id);
        const cityIds = cities.map(city => city.id);

        const filters = {
            minPrice: priceRange[0] || 0,
            maxPrice: priceRange[1] || 1000,
            categoryIds: categoryIds,
            cityIds: cityIds,
            page: 0,
            pageSize: 5,
            sortingField: 'updatedAt',
            sortingDirection: 'DESC',
        };

        applyFilters(filters);
    };

    return (
        <div>
            <h2>Filter</h2>
            <FilterElement
                title='Cities'
                data={cities}
                chosen={selectedCities}
                setChosen={setSelectedCities}
            />
            <FilterElement
                title='Categories'
                data={categories}
                chosen={selectedCategories}
                setChosen={setSelectedCategories}
            />
            <label>
                Price: {priceRange[0]} - {priceRange[1]}
                <Slider
                    min={0}
                    max={1000}
                    range
                    defaultValue={priceRange}
                    onChange={handlePriceRangeChange}
                />
            </label>
            <button onClick={handleApply}>
                Apply
            </button>
        </div>
    );
}

export default Filters;
