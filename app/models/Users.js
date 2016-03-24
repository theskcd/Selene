var mongoose=require('mongoose');
var Schema=mongoose.Schema;

var UserSchema=new Schema({
	fname:{type:String,required:true},
	lname:{type:String,required:true},
	email:{type:String},
	facebook:{
		id:{type:String,required:true,uniqe:true}
	}
});

module.exports=mongoose.model('Users',UserSchema);