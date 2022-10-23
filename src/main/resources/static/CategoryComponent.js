const CategoryLi = {
	name: "category-li",
	props : {
		id : Number,
		parent : Number,
		depth : Number,
		child_count : Number,
		name : String,
		events: Object,
		search : Boolean
	},
	data() {
	    return {
	      loaded: false,
		  children : [],
		  parents : []
	    }
    },
    computed: {
	    fullname() {
		  	if(this.depth == 0)
		  		return `/${this.name}`
		  	
		  	if(this.parents.length == 0)
		  		return `/${this.name}`
		  	
		  	let parentslength=this.parents.length;
		  	let compteur = 0;
		  	let currentid = 0;
		  	let fullpath = "";
		  	while (compteur < parentslength){
				  // find parent
				  if(compteur == 0){
					  for(let i = 0; i < this.parents.length; i++){
						  if(this.parents[i].parent == null) {
							  fullpath += `/${this.parents[i].name}`;
							  compteur++;
							  currentid = this.parents[i].id;
						  }
					  }
				  } else {
					  for(let i = 0; i < this.parents.length; i++){
						  if(this.parents[i].parent == currentid) {
							  fullpath += `/${this.parents[i].name}`;
							  compteur++;
							  currentid = this.parents[i].id;
						  }
					  }
					  
				  }
			}
	      	return `${fullpath}/${this.name}`
	    }
	  },
	watch: {
		events: {
	      handler: function (val, oldVal) {
	        // Check last item
	        let event = val.slice(-1)[0];
	        
	        if(event.data.parent == this.id){
	        	this.loadChildren();
			}
	        	
	        // is child
	        for(let i=0;i<this.children.length;i++) {
		        if(event.data.parent == this.children[i].id){
		        	this.loadChildren();
				}
			}
	      },
	      deep: true
	    },
	},
	methods : {
		toggleChildren(id){
			let selector="#c-"+id
			
			document.querySelector(selector).classList.toggle("caret-down");
			document.querySelector(selector + "-ul").classList.toggle("active");
			
			this.loadChildren();
		},
				
		async loadParents(){
		    		    
		    try{
				const response = await fetch(`/api/v1/views/${this.id}/parents`);
			    const json = await response.json();
				this.parents = json;
			} catch(error){
				console.error(error);
				vueApp.showErrorModal(error);
			}
		},
		
		addChild(){
			let name = prompt(`Adding child to ${this.name}. Type Child name:`);
			if (name == null || name == "") {
				// User Canceled
			} else {
				vueApp.addCategory(name, this.id);
			} 
		},
		
		editName(){
			let name = prompt(`Editing ${this.name} name. Type new name:`, this.name);
			if (name == null || name == "") {
				// User Canceled
			} else {
				vueApp.editName(name, this.id, this.name);
			} 
		},
		deleteNode(){
			 if(window.confirm(`Deleting ${this.name}, are you sure ?`)){
				vueApp.deleteCategory(this.id);
			 }
		},
		
		async loadChildren(){
		    this.loaded=false;
		    
		    try{
				const response = await fetch(`/api/v1/views/${this.id}/children`);
			    const json = await response.json();
	
			    this.children=json;
			} catch(error){
				console.error(error);
				vueApp.showErrorModal(error);
			}
		    this.loaded=true;
		}
	},	
	mounted() {
		
		if(this.search && this.depth > 0)
			this.loadParents();
		
	},
	template : 
		`
    	<li v-if="!search" style="padding:1px">
    		<span :id="'c-'+id" class="caret" :class="{ 'caret-hidden' : child_count==0 }" @click="toggleChildren(id)">{{name}}</span>&nbsp;
    		<span class="badge bg-secondary" v-if="child_count > 0">{{child_count}}</span>&nbsp;
    		
    		
    		<button class="btn btn-light" v-if="child_count < 10 && depth < 10" title="Add Child" @click="addChild()"><i class="bi bi-plus-circle-fill"></i></button>
    		<button class="btn btn-light" title="Edit Name" @click="editName()"><i class="bi bi-pencil-fill"></i></button>
    		<button class="btn btn-light" v-if="child_count == 0" title="Delete Category" @click="deleteNode()"><i class="bi bi-trash-fill"></i></button>
    		<ul :id="'c-'+id+'-ul'" class="nested">
    			<div v-if="!loaded && child_count > 0" class="spinner-border text-secondary" role="status"><span class="visually-hidden">Loading...</span></div>
		    	<category-li v-for="cat in children"
		    		:id="cat.id"
		    		:parent="cat.parent"
		    		:depth="cat.depth"
		    		:child_count="cat.child_count"
		    		:name="cat.name"
		    		:events="events"></category-li>
    		</ul>
    	</li>
    	<li v-if="search" style="padding:1px">
    		{{fullname}}&nbsp;
    		<span class="badge bg-secondary" v-if="child_count > 0">{{child_count}}</span>&nbsp;
    		<button class="btn btn-light" title="Edit Name" @click="editName()"><i class="bi bi-pencil-fill"></i></button>
    		<button class="btn btn-light" v-if="child_count == 0" title="Delete Category" @click="deleteNode()"><i class="bi bi-trash-fill"></i></button>
    	</li>
    	`
}