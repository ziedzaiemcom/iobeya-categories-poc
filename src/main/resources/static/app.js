const { createApp } = Vue

const vueApp = createApp({
	data() {
		return {
			// Root Categories
			categories : {
				loaded : true,
				data : false
			},
			// Events Bus
			events : [],
			//Websocket State
			ws_state : false,
			// Search Module
			search : {
				term : "",
				loaded : true,
				data : false
			},
			// Stats Module
			stats : {
				total : [],
				byDepth : []
			},
			// Error Modal
			errorModal: {
				title : "Error",
				msg : ""
			}
		}
	},
	components : {
		// COmponent to render a category and its children recursively
		'CategoryLi' : CategoryLi
	},
	watch: {
		// Handle Realtime Events Globally
		events: {
	      handler: function (val, oldVal) {
	        // Check last item
	        let event = val.slice(-1)[0];
	        
	        // A new Root category has been Created/Edited/deleted
	        if(event.data.parent == null)
	        	this.fetchRootCategories();
	        	
	        // is child
	        for(let i=0;i<this.categories.data.length;i++) {
		        if(event.data.parent == this.categories.data[i].id){
	        		this.fetchRootCategories();
					
				}
			}
			// Refresh Search Results View
			this.searchCategories(this.search.term);
			
			// Refresh Stats
			this.loadStats();
			this.loadStatsByDepth();
	      },
	      deep: true
	    }
	},
	methods : {
		
		
		// Fetch Root categories via REST API
		async fetchRootCategories() {
				
			// Show loaded
			this.categories.loaded = false;
			
			try{
				const response = await fetch(`/api/v1/views/0`);
			    const json = await response.json();
			    
				this.categories.data = json;
				
			} catch(error){
				this.showErrorModal(error);
				console.log(error);
			}
			
			this.categories.loaded = true;
		},
		
		// Add a new Root categopry
		addRootCategory(){
			let name = prompt(`Adding Root category. Type name:`);
			if (name == null || name == "") {
				// User Canceled
			} else {
				this.addCategory(name, null);
			} 
		},
		
		// General method to call REST API to create a new category
		async addCategory(name, parent) {
			const child = {
				name : name,
				parent : parent
			}
			
			try{
				const response = await fetch(`/api/v1/categories`, {
    				method: 'POST',
			        headers: {
				      'Content-Type': 'application/json'
				    },
			    	body: JSON.stringify(child)
    			});
    			
    			if(!response.ok) {
					const error = await response.text();
					throw error;
				}
    			
			    const json = await response.json();
			    // Check Result
				
			} catch(error){
				console.error(error);
				this.showErrorModal(error);
			}
		},
		
		async editName(newName, id, name) {
			const child = {
				name : newName,
				id : id
			}
			
			try{
				const response = await fetch(`/api/v1/categories/${id}`, {
    				method: 'PATCH',
			        headers: {
				      'Content-Type': 'application/json'
				    },
			    	body: JSON.stringify(child)
    			});
    			
    			if(!response.ok) {
					const error = await response.text();
					throw error;
				}
    			
			    const json = await response.json();
			} catch(error){
				console.error(error);
				this.showErrorModal(error);
			}
		},
		
		async deleteCategory(id){
			
			try{
				const response = await fetch(`/api/v1/categories/${id}`, {
    				method: 'DELETE'
    			});
    			
    			if(!response.ok) {
					const error = await response.text();
					throw error;
				}
				
			    const json = await response.json();
				
			} catch(error){
				this.showErrorModal(error);
				console.error(error);
			}
			
		},
		
		// Search 
		searchFor(term){
			this.searchCategories(term);
		},
		
		
		async searchCategories(term) {
			if(!term || term.length == 0)
				return;
				
			// Show loaded
			this.search.loaded = false;
			
			try{
				const response = await fetch(`/api/v1/search/${term}`);
			    const json = await response.json();
			    
				this.search.data = json;
				
			} catch(error){
				this.showErrorModal(error);
				console.log(error);
			}
			
			this.search.loaded = true;
		},
		
		// Stats Module Methods
		async loadStats(){
			
			try{
				const response = await fetch(`/api/v1/stats`);
			    const json = await response.json();
			    
			    this.stats.total = json;
				
			} catch(error){
				this.showErrorModal(error);
				console.log(error);
			}
			
		},
		
		async loadStatsByDepth(){
			
			try{
				const response = await fetch(`/api/v1/stats/depth`);
			    const json = await response.json();
			    
			    this.stats.byDepth = json;
				
			} catch(error){
				this.showErrorModal(error);
				console.log(error);
			}
			
		},
		
		// Events 
		getEventIcon(ev){
			if(ev.type == 'C')
				return `<i class="bi bi-plus-circle-fill"></i>`
			else if(ev.type == 'M')
				return `<i class="bi bi-pencil-fill"></i>`
			else if(ev.type == 'D')
				return `<i class="bi bi-trash-fill"></i>`
			
		},
		getEventAgo(ev){
			return moment(ev.timestamp).fromNow();
		},
		//Modal events
		showErrorModal(error){
			this.errorModal.msg = error;
			const myModal = new bootstrap.Modal(document.getElementById('errorModal'));
			myModal.show()
		}
		
	},
	mounted() {
		// Load Root Categories
		this.fetchRootCategories();
		
		// Load Stats
		this.loadStats();
		this.loadStatsByDepth();
	}
}).mount("#app");