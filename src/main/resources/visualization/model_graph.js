// Global variables
let cy = null;
const emptyMessage = document.getElementById('empty-message');

// Initialize Cytoscape with basic configuration
function initCytoscape() {
    console.log("Initializing Cytoscape.js");
    try {
        cy = cytoscape({
            container: document.getElementById('cy'),
            elements: [],
            style: [
                {
                    selector: 'node',
                    style: {
                        'background-color': '#4f81bd',
                        'label': 'data(label)',
                        'color': '#fff',
                        'text-valign': 'center',
                        'text-halign': 'center',
                        'width': 'label',
                        'height': 'label',
                        'padding': '10px',
                        'font-size': '12px',
                        'shape': 'round-rectangle',
                        'border-width': '1px',
                        'border-color': '#2a5784',
                        'text-outline-width': '1px',
                        'text-outline-color': '#2a5784',
                        'text-outline-opacity': 0.5
                    }
                },
                {
                    selector: 'edge',
                    style: {
                        'width': 2,
                        'line-color': '#9bbb59',
                        'target-arrow-color': '#9bbb59',
                        'target-arrow-shape': 'triangle',
                        'curve-style': 'bezier',
                        'label': 'data(label)',
                        'text-background-color': '#ffffff',
                        'text-background-opacity': 0.7,
                        'text-background-padding': 3,
                        'text-background-shape': 'round-rectangle',
                        'font-size': '10px',
                        'color': '#333333'
                    }
                }
            ],
            // Use simple layouts for better stability
            layout: {
                name: 'grid',
                padding: 50
            }
        });
        console.log("Cytoscape initialized successfully");
        return true;
    } catch(e) {
        console.error("Error initializing Cytoscape:", e);
        emptyMessage.textContent = "Ошибка инициализации графа: " + e.message;
        return false;
    }
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    console.log("DOM loaded, initializing visualization");
    initCytoscape();
});

// Try to initialize immediately as well
try {
    initCytoscape();
} catch(e) {
    console.error("Error in immediate initialization:", e);
}

// Update visualization with new data from Java
function updateVisualization(jsonData) {
    console.log("updateVisualization called");
    try {
        if (!cy) {
            console.log("No Cytoscape instance, initializing");
            if (!initCytoscape()) {
                console.error("Failed to initialize Cytoscape");
                emptyMessage.textContent = "Ошибка: Не удалось инициализировать граф";
                emptyMessage.style.display = 'block';
                return;
            }
        }
        
        // Parse JSON data safely
        let data = [];
        try {
            data = JSON.parse(jsonData);
            console.log("Parsed JSON data, elements:", (data ? data.length : 0));
        } catch (parseError) {
            console.error("Error parsing JSON:", parseError);
            emptyMessage.textContent = "Ошибка: Неверный формат данных";
            emptyMessage.style.display = 'block';
            return;
        }
        
        // Clear existing elements safely
        try {
            cy.elements().remove();
        } catch (removeError) {
            console.error("Error removing elements:", removeError);
        }
        
        // Show empty message if no data
        if (!data || data.length === 0) {
            console.log("No elements to display");
            emptyMessage.style.display = 'block';
            return;
        }
        
        // Hide empty message
        emptyMessage.style.display = 'none';
        
        // Add elements to graph
        try {
            cy.add(data);
            console.log("Added elements to graph, count:", cy.elements().length);
        } catch (addError) {
            console.error("Error adding elements:", addError);
            emptyMessage.textContent = "Ошибка: Не удалось добавить элементы графа";
            emptyMessage.style.display = 'block';
            return;
        }
        
        // Apply a simple grid layout
        console.log("Applying grid layout");
        try {
            cy.layout({
                name: 'grid',
                padding: 50,
                fit: true,
                avoidOverlap: true
            }).run();
            
            // Ensure graph is centered and visible
            setTimeout(function() {
                try {
                    cy.center();
                    cy.fit();
                    console.log("Graph centered and fitted to view");
                } catch(e) {
                    console.error("Error in delayed centering:", e);
                }
            }, 100);
            
        } catch (layoutError) {
            console.error("Error applying layout:", layoutError);
            // Even if layout fails, try to make the graph visible
            try {
                cy.fit();
            } catch (e) {
                console.error("Error fitting graph:", e);
            }
        }
        
    } catch (e) {
        console.error("Error updating visualization:", e);
        emptyMessage.textContent = "Ошибка: " + e.message;
        emptyMessage.style.display = 'block';
    }
}

// Refresh visualization (called from Java)
function refreshVisualization() {
    console.log("Refreshing visualization");
    try {
        if (typeof cy !== 'undefined' && cy) {
            console.log("Cytoscape instance found, refreshing layout");
            
            // Get element count with safeguards
            let elemCount = 0;
            try {
                elemCount = cy.elements().length;
                console.log("Graph has " + elemCount + " elements");
            } catch(err) {
                console.error("Error getting element count:", err);
            }
            
            if (elemCount > 0) {
                // Apply simple grid layout for stability
                console.log("Applying grid layout");
                cy.layout({
                    name: 'grid',
                    padding: 50,
                    fit: true,
                    avoidOverlap: true
                }).run();
                
                // Use both center and fit for best positioning
                console.log("Centering and fitting graph");
                cy.center();
                cy.fit();
            } else {
                console.log("No elements to display");
            }
        } else if (typeof initCytoscape === 'function') {
            console.log("No Cytoscape instance, reinitializing");
            initCytoscape();
        } else {
            console.log("No Cytoscape functions available");
        }
    } catch(e) {
        console.error("Error refreshing visualization:", e);
    }
}

// Ensure functions are available to Java
window.updateVisualization = updateVisualization;
window.refreshVisualization = refreshVisualization;
console.log("Visualization ready");